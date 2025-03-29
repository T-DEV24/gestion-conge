package com.ismat.conge.controller;

import com.ismat.conge.entitie.Conge;
import com.ismat.conge.exception.EntityNotFoundException;
import com.ismat.conge.payload.in.CreateCongePayload;
import com.ismat.conge.service.CongeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/conges")
public class CongeController {

    private final CongeService congeService;

    @Autowired
    public CongeController(CongeService congeService) {
        this.congeService = congeService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    @GetMapping("/demander")
    public String showDemanderCongeForm(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("congePayload", new CreateCongePayload());
        return "conge/demander";
    }

    @PostMapping("/demander")
    public String demanderConge(@Valid @ModelAttribute("congePayload") CreateCongePayload payload, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            congeService.demanderConge(payload);
            return "redirect:/conges?message=Demande de congé enregistrée avec succès";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la demande : " + e.getMessage());
            return "conge/demander";
        }
    }

    @GetMapping
    public String getAllConges(Model model, @RequestParam(required = false) String message, @RequestParam(required = false) String error, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("conges", congeService.getAllConges());
        if (message != null) model.addAttribute("message", message);
        if (error != null) model.addAttribute("error", error);
        return "conge/list";
    }

    @GetMapping("/personnel/{idPersonnel}")
    public String getCongesByPersonnel(@PathVariable int idPersonnel, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            model.addAttribute("conges", congeService.getCongesByPersonnel(idPersonnel));
            return "conge/list";
        } catch (EntityNotFoundException e) {
            return "redirect:/conges?error=" + e.getMessage();
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditCongeForm(@PathVariable int id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            Conge conge = congeService.getCongeById(id);
            CreateCongePayload payload = new CreateCongePayload(conge.getDureeMax(), conge.getType().name(), conge.getPersonnel().getIdPersonnel());
            model.addAttribute("congePayload", payload);
            model.addAttribute("idConge", id);
            return "conge/edit";
        } catch (EntityNotFoundException e) {
            return "redirect:/conges?error=" + e.getMessage();
        }
    }

    @PostMapping("/edit/{id}")
    public String updateConge(@PathVariable int id, @Valid @ModelAttribute("congePayload") CreateCongePayload payload, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            congeService.updateConge(id, payload);
            return "redirect:/conges?message=Congé mis à jour avec succès";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            model.addAttribute("idConge", id);
            return "conge/edit";
        }
    }

    @PostMapping("/{id}/statut")
    public String updateStatutConge(@PathVariable int id, @RequestParam String statut, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            congeService.updateStatutConge(id, statut);
            return "redirect:/conges?message=Statut mis à jour avec succès";
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return "redirect:/conges?error=" + e.getMessage();
        }
    }

    @PostMapping("/delete/{id}")
    public String supprimerConge(@PathVariable int id, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        try {
            congeService.supprimerConge(id);
            return "redirect:/conges?message=Congé supprimé avec succès";
        } catch (EntityNotFoundException e) {
            return "redirect:/conges?error=" + e.getMessage();
        }
    }

    @GetMapping("/stats/type")
    public String getStatsParType(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("stats", congeService.getStatsParType());
        return "conge/stats-type";
    }

    @GetMapping("/stats/departement")
    public String getStatsParDepartement(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/personnels/login";
        }
        model.addAttribute("stats", congeService.getStatsParDepartement());
        return "conge/stats-departement";
    }
}