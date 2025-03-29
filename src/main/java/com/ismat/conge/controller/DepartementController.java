package com.ismat.conge.controller;

import com.ismat.conge.entitie.Departement;
import com.ismat.conge.exception.EntityNotFoundException;
import com.ismat.conge.payload.in.CreateDepartementPayload;
import com.ismat.conge.service.DepartementService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departements")
public class DepartementController {

    private final DepartementService departementService;

    @Autowired
    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    @GetMapping("/new")
    public String showCreateDepartementForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("departementPayload", new CreateDepartementPayload());
        return "departement/new";
    }

    @PostMapping("/new")
    public String createDepartement(@Valid @ModelAttribute("departementPayload") CreateDepartementPayload payload, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            departementService.createDepartement(payload);
            return "redirect:/departements?message=Département créé avec succès";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "departement/new";
        }
    }

    @GetMapping
    public String getAllDepartements(Model model, @RequestParam(required = false) String message, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("departements", departementService.getAllDepartements());
        if (message != null) model.addAttribute("message", message);
        return "departement/list";
    }

    @GetMapping("/{id}")
    public String getDepartementById(@PathVariable int id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            model.addAttribute("departement", departementService.getDepartementById(id));
            return "departement/details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/departements";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditDepartementForm(@PathVariable int id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            Departement departement = departementService.getDepartementById(id);
            CreateDepartementPayload payload = new CreateDepartementPayload(departement.getNom(), departement.getAdresse());
            model.addAttribute("departementPayload", payload);
            model.addAttribute("idDepartement", id);
            return "departement/edit";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/departements";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateDepartement(@PathVariable int id, @Valid @ModelAttribute("departementPayload") CreateDepartementPayload payload, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            departementService.updateDepartement(id, payload);
            return "redirect:/departements?message=Département mis à jour avec succès";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "departement/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteDepartement(@PathVariable int id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            departementService.deleteDepartement(id);
            return "redirect:/departements?message=Département supprimé avec succès";
        } catch (EntityNotFoundException e) {
            return "redirect:/departements?error=" + e.getMessage();
        }
    }
}