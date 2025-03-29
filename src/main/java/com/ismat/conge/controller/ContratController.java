package com.ismat.conge.controller;

import com.ismat.conge.entitie.Contrat;
import com.ismat.conge.exception.EntityNotFoundException;
import com.ismat.conge.payload.in.CreateContratPayload;
import com.ismat.conge.service.ContratService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contrats")
public class ContratController {

    private final ContratService contratService;

    @Autowired
    public ContratController(ContratService contratService) {
        this.contratService = contratService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    @GetMapping("/new")
    public String showCreateContratForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("contratPayload", new CreateContratPayload());
        return "contrat/new";
    }

    @PostMapping("/new")
    public String createContrat(@Valid @ModelAttribute("contratPayload") CreateContratPayload payload, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            contratService.createContrat(payload);
            return "redirect:/contrats?message=Contrat créé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "contrat/new";
        }
    }

    @GetMapping
    public String getAllContrats(Model model, @RequestParam(required = false) String message, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("contrats", contratService.getAllContrats());
        if (message != null) model.addAttribute("message", message);
        return "contrat/list";
    }

    @GetMapping("/{id}")
    public String getContratById(@PathVariable int id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            model.addAttribute("contrat", contratService.getContratById(id));
            return "contrat/details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/contrats";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditContratForm(@PathVariable int id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            Contrat contrat = contratService.getContratById(id);
            CreateContratPayload payload = new CreateContratPayload(
                    contrat.getNumeroContrat(),
                    contrat.getDateContrat(),
                    contrat.getDureeContrat(),
                    contrat.getTypeContrat().name(),
                    contrat.getPersonnel().getIdPersonnel()
            );
            model.addAttribute("contratPayload", payload);
            model.addAttribute("idContrat", id);
            return "contrat/edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/contrats";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateContrat(@PathVariable int id, @Valid @ModelAttribute("contratPayload") CreateContratPayload payload, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            contratService.updateContrat(id, payload);
            return "redirect:/contrats?message=Contrat mis à jour avec succès";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "contrat/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteContrat(@PathVariable int id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            contratService.deleteContrat(id);
            return "redirect:/contrats?message=Contrat supprimé avec succès";
        } catch (EntityNotFoundException | EmptyResultDataAccessException e) {
            return "redirect:/contrats?error=Contrat non trouvé avec l'ID " + id;
        } catch (DataIntegrityViolationException e) {
            return "redirect:/contrats?error=Impossible de supprimer le contrat en raison de contraintes dans la base de données";
        } catch (Exception e) {
            return "redirect:/contrats?error=Erreur inattendue lors de la suppression : " + e.getMessage();
        }
    }
}