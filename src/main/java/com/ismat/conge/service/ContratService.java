package com.ismat.conge.service;

import com.ismat.conge.entitie.Contrat;
import com.ismat.conge.entitie.Personnel;
import com.ismat.conge.payload.in.CreateContratPayload;
import com.ismat.conge.repository.ContratRepository;
import com.ismat.conge.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratService {

    private final ContratRepository contratRepository;
    private final PersonnelRepository personnelRepository;

    @Autowired
    public ContratService(ContratRepository contratRepository, PersonnelRepository personnelRepository) {
        this.contratRepository = contratRepository;
        this.personnelRepository = personnelRepository;
    }

    public Contrat createContrat(CreateContratPayload payload) {
        Personnel personnel = personnelRepository.findById(payload.getIdPersonnel())
                .orElseThrow(() -> new RuntimeException("Personnel non trouvé"));
        Contrat contrat = Contrat.builder()
                .numeroContrat(payload.getNumeroContrat())
                .dateContrat(payload.getDateContrat())
                .dureeContrat(payload.getDureeContrat())
                .typeContrat(Contrat.TypeContrat.valueOf(payload.getTypeContrat().toUpperCase()))
                .personnel(personnel)
                .build();
        return contratRepository.save(contrat);
    }

    public List<Contrat> getAllContrats() {
        return contratRepository.findAll();
    }

    public Contrat getContratById(int idContrat) {
        return contratRepository.findById(idContrat)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));
    }

    public Contrat getContratByPersonnel(int idPersonnel) {
        return contratRepository.findByPersonnelIdPersonnel(idPersonnel);
    }

    public Contrat updateContrat(int idContrat, CreateContratPayload payload) {
        Contrat contrat = contratRepository.findById(idContrat)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));
        Personnel personnel = personnelRepository.findById(payload.getIdPersonnel())
                .orElseThrow(() -> new RuntimeException("Personnel non trouvé"));
        contrat.setNumeroContrat(payload.getNumeroContrat());
        contrat.setDateContrat(payload.getDateContrat()); // Ajout
        contrat.setDureeContrat(payload.getDureeContrat());
        contrat.setTypeContrat(Contrat.TypeContrat.valueOf(payload.getTypeContrat().toUpperCase()));
        contrat.setPersonnel(personnel);
        return contratRepository.save(contrat);
    }

    public void deleteContrat(int idContrat) {
        contratRepository.deleteById(idContrat);
    }
}