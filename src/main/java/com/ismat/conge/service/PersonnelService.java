package com.ismat.conge.service;

import com.ismat.conge.entitie.Departement;
import com.ismat.conge.entitie.Personnel;
import com.ismat.conge.exception.EntityNotFoundException;
import com.ismat.conge.payload.in.CreatePersonnelPayload;
import com.ismat.conge.repository.DepartementRepository;
import com.ismat.conge.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final DepartementRepository departementRepository;

    @Autowired
    public PersonnelService(PersonnelRepository personnelRepository, DepartementRepository departementRepository) {
        this.personnelRepository = personnelRepository;
        this.departementRepository = departementRepository;
    }

    public Personnel createPersonnel(CreatePersonnelPayload payload) {
        Departement departement = departementRepository.findById(payload.getIdDepartement())
                .orElseThrow(() -> new EntityNotFoundException("Département non trouvé"));
        Personnel personnel = Personnel.builder()
                .nom(payload.getNom())
                .prenom(payload.getPrenom())
                .adresse(payload.getAdresse())
                .dateNaissance(payload.getDateNaissance())
                .telephone(payload.getTelephone())
                .email(payload.getEmail())
                .password(payload.getPassword())
                .departement(departement)
                .build();
        return personnelRepository.save(personnel);
    }

    public List<Personnel> getAllPersonnel() {
        return personnelRepository.findAll();
    }

    public Personnel getPersonnelById(int idPersonnel) {
        return personnelRepository.findById(idPersonnel)
                .orElseThrow(() -> new EntityNotFoundException("Personnel non trouvé avec l'ID : " + idPersonnel));
    }

    public List<Personnel> getPersonnelByDepartement(int idDepartement) {
        return personnelRepository.findByDepartementIdDepartement(idDepartement);
    }

    public Personnel updatePersonnel(int idPersonnel, CreatePersonnelPayload payload) {
        Personnel personnel = personnelRepository.findById(idPersonnel)
                .orElseThrow(() -> new EntityNotFoundException("Personnel non trouvé avec l'ID : " + idPersonnel));
        Departement departement = departementRepository.findById(payload.getIdDepartement())
                .orElseThrow(() -> new EntityNotFoundException("Département non trouvé"));
        personnel.setNom(payload.getNom());
        personnel.setPrenom(payload.getPrenom());
        personnel.setAdresse(payload.getAdresse());
        personnel.setDateNaissance(payload.getDateNaissance());
        personnel.setTelephone(payload.getTelephone());
        personnel.setEmail(payload.getEmail());
        personnel.setPassword(payload.getPassword());
        personnel.setDepartement(departement);
        return personnelRepository.save(personnel);
    }

    public void deletePersonnel(int idPersonnel) {
        if (!personnelRepository.existsById(idPersonnel)) {
            throw new EntityNotFoundException("Personnel non trouvé avec l'ID : " + idPersonnel);
        }
        personnelRepository.deleteById(idPersonnel);
    }

    // Nouvelle méthode pour la connexion
    public Personnel login(String email, String password) {
        Personnel personnel = personnelRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Aucun personnel trouvé avec l'email : " + email));
        if (!personnel.getPassword().equals(password)) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }
        return personnel;
    }
}