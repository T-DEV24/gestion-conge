package com.ismat.conge.service;

import com.ismat.conge.entitie.Conge;
import com.ismat.conge.entitie.Personnel;
import com.ismat.conge.exception.EntityNotFoundException;
import com.ismat.conge.payload.in.CreateCongePayload;
import com.ismat.conge.repository.CongeRepository;
import com.ismat.conge.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CongeService {

    private final CongeRepository congeRepository;
    private final PersonnelRepository personnelRepository;

    @Autowired
    public CongeService(CongeRepository congeRepository, PersonnelRepository personnelRepository) {
        this.congeRepository = congeRepository;
        this.personnelRepository = personnelRepository;
    }

    public Conge demanderConge(CreateCongePayload payload) {
        if (payload.getDureeMax() == null || payload.getDureeMax() <= 0) {
            throw new IllegalArgumentException("La durée maximale doit être positive");
        }
        Personnel personnel = personnelRepository.findById(payload.getIdPersonnel())
                .orElseThrow(() -> new EntityNotFoundException("Personnel non trouvé avec l'ID : " + payload.getIdPersonnel()));
        try {
            Conge.Type type = Conge.Type.valueOf(payload.getType().toUpperCase());
            Conge conge = Conge.builder()
                    .dureeMax(payload.getDureeMax())
                    .type(type)
                    .personnel(personnel)
                    .build(); // statut par défaut à EN_ATTENTE via @Builder.Default
            return congeRepository.save(conge);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de congé invalide : " + payload.getType());
        }
    }

    public Conge updateStatutConge(int idConge, String statut) {
        Conge conge = congeRepository.findById(idConge)
                .orElseThrow(() -> new EntityNotFoundException("Congé non trouvé avec l'ID : " + idConge));
        try {
            conge.setStatut(Conge.Statut.valueOf(statut.toUpperCase()));
            return congeRepository.save(conge);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide : " + statut);
        }
    }

    public void supprimerConge(int idConge) {
        if (!congeRepository.existsById(idConge)) {
            throw new EntityNotFoundException("Congé non trouvé avec l'ID : " + idConge);
        }
        congeRepository.deleteById(idConge);
    }

    public List<Conge> getAllConges() {
        return congeRepository.findAll();
    }

    public List<Conge> getCongesByPersonnel(int idPersonnel) {
        if (!personnelRepository.existsById(idPersonnel)) {
            throw new EntityNotFoundException("Personnel non trouvé avec l'ID : " + idPersonnel);
        }
        return congeRepository.findByPersonnelIdPersonnel(idPersonnel);
    }

    public Map<Conge.Type, Long> getStatsParType() {
        Map<Conge.Type, Long> stats = new HashMap<>();
        for (Conge.Type type : Conge.Type.values()) {
            stats.put(type, congeRepository.countByType(type));
        }
        return stats;
    }

    public Map<String, Long> getStatsParDepartement() {
        Map<String, Long> stats = new HashMap<>();
        personnelRepository.findAll().forEach(personnel -> {
            String deptNom = personnel.getDepartement().getNom();
            stats.put(deptNom, congeRepository.countByPersonnelDepartementIdDepartement(
                    personnel.getDepartement().getIdDepartement()));
        });
        return stats;
    }

    public Conge updateConge(int idConge, CreateCongePayload payload) {
        if (payload.getDureeMax() == null || payload.getDureeMax() <= 0) {
            throw new IllegalArgumentException("La durée maximale doit être positive");
        }
        Conge conge = congeRepository.findById(idConge)
                .orElseThrow(() -> new EntityNotFoundException("Congé non trouvé avec l'ID : " + idConge));
        Personnel personnel = personnelRepository.findById(payload.getIdPersonnel())
                .orElseThrow(() -> new EntityNotFoundException("Personnel non trouvé avec l'ID : " + payload.getIdPersonnel()));
        try {
            conge.setDureeMax(payload.getDureeMax());
            conge.setType(Conge.Type.valueOf(payload.getType().toUpperCase()));
            conge.setPersonnel(personnel);
            return congeRepository.save(conge);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de congé invalide : " + payload.getType());
        }
    }

    public Conge getCongeById(int idConge) {
        return congeRepository.findById(idConge)
                .orElseThrow(() -> new EntityNotFoundException("Congé non trouvé avec l'ID : " + idConge));
    }
}