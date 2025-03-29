package com.ismat.conge.controller;

import com.ismat.conge.entitie.Personnel;
import com.ismat.conge.exception.EntityNotFoundException;
import com.ismat.conge.payload.in.CreatePersonnelPayload;
import com.ismat.conge.payload.in.LoginPayload;
import com.ismat.conge.service.CongeService;
import com.ismat.conge.service.ContratService;
import com.ismat.conge.service.DepartementService;
import com.ismat.conge.service.PersonnelService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personnels")
public class PersonnelController {

    private final PersonnelService personnelService;
    private final DepartementService departementService;
    private final ContratService contratService;
    private final CongeService congeService;

    @Autowired
    public PersonnelController(PersonnelService personnelService, DepartementService departementService,
                               ContratService contratService, CongeService congeService) {
        this.personnelService = personnelService;
        this.departementService = departementService;
        this.contratService = contratService;
        this.congeService = congeService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    @GetMapping("/new")
    public String showCreatePersonnelForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("personnelPayload", new CreatePersonnelPayload());
        return "personnel/new";
    }

    @PostMapping("/new")
    public String createPersonnel(@Valid @ModelAttribute("personnelPayload") CreatePersonnelPayload payload,
                                  BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        if (bindingResult.hasErrors()) {
            return "personnel/new";
        }
        try {
            personnelService.createPersonnel(payload);
            redirectAttributes.addFlashAttribute("message", "Utilisateur créé avec succès");
            return "redirect:/personnels/new";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Un utilisateur avec cet email existe déjà");
            return "personnel/new";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "personnel/new";
        }
    }

    @GetMapping
    public String getAllPersonnel(Model model, @RequestParam(required = false) String message, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("personnels", personnelService.getAllPersonnel());
        if (message != null) model.addAttribute("message", message);
        return "personnel/list";
    }

    @GetMapping("/{id}")
    public String getPersonnelById(@PathVariable("id") Integer id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            Personnel personnel = personnelService.getPersonnelById(id);
            model.addAttribute("personnel", personnel);
            return "personnel/details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/personnels";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditPersonnelForm(@PathVariable("id") Integer id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            Personnel personnel = personnelService.getPersonnelById(id);
            CreatePersonnelPayload payload = new CreatePersonnelPayload(
                    personnel.getIdPersonnel(),
                    personnel.getNom(),
                    personnel.getPrenom(),
                    personnel.getAdresse(),
                    personnel.getDateNaissance(),
                    personnel.getTelephone(),
                    personnel.getEmail(),
                    personnel.getPassword(),
                    personnel.getDepartement().getIdDepartement()
            );
            model.addAttribute("personnelPayload", payload);
            System.out.println("Payload pour édition : " + payload);
            return "personnel/edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/personnels";
        }
    }

    @PostMapping("/edit/{id}")
    public String updatePersonnel(@PathVariable("id") Integer id,
                                  @Valid @ModelAttribute("personnelPayload") CreatePersonnelPayload payload,
                                  BindingResult bindingResult, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        if (bindingResult.hasErrors()) {
            return "personnel/edit";
        }
        try {
            personnelService.updatePersonnel(id, payload);
            return "redirect:/personnels?message=Personnel mis à jour avec succès";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "personnel/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deletePersonnel(@PathVariable("id") Integer id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            personnelService.deletePersonnel(id);
            return "redirect:/personnels?message=Personnel supprimé avec succès";
        } catch (EntityNotFoundException e) {
            return "redirect:/personnels?error=" + e.getMessage();
        } catch (DataIntegrityViolationException e) {
            return "redirect:/personnels?error=Impossible de supprimer le personnel en raison de contraintes dans la base de données";
        } catch (Exception e) {
            return "redirect:/personnels?error=Erreur inattendue : " + e.getMessage();
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginPayload", new LoginPayload());
        return "personnel/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginPayload") LoginPayload payload,
                               BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "personnel/login";
        }
        try {
            Personnel personnel = personnelService.login(payload.getEmail(), payload.getPassword());
            session.setAttribute("loggedInUser", personnel);
            return "redirect:/personnels/dashboard";
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            model.addAttribute("error", "Email ou mot de passe incorrect");
            return "personnel/login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        Personnel loggedInUser = (Personnel) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("nbDepartements", departementService.getAllDepartements().size());
        model.addAttribute("nbPersonnels", personnelService.getAllPersonnel().size());
        model.addAttribute("nbContrats", contratService.getAllContrats().size());
        model.addAttribute("nbConges", congeService.getAllConges().size());
        model.addAttribute("user", loggedInUser);
        return "dashboard";
    }

    @GetMapping("/dashboard-personnel")
    public String showPersonnelDashboard(Model model, HttpSession session) {
        Personnel loggedInUser = (Personnel) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/personnels/login";
        }
        int idPersonnel = loggedInUser.getIdPersonnel();
        model.addAttribute("nbConges", congeService.getCongesByPersonnel(idPersonnel).size());
        model.addAttribute("nbContrats", contratService.getContratByPersonnel(idPersonnel) != null ? 1 : 0);
        model.addAttribute("departement", loggedInUser.getDepartement().getNom());
        model.addAttribute("user", loggedInUser);
        return "personnel/dashboard";
    }
}