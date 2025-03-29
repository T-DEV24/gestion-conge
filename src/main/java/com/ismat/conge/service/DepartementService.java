package com.ismat.conge.service;

import com.ismat.conge.entitie.Departement;
import com.ismat.conge.payload.in.CreateDepartementPayload;
import com.ismat.conge.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService implements CommandLineRunner {

    private final DepartementRepository departementRepository;

    @Autowired
    public DepartementService(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    // Créer un département
    public Departement createDepartement(CreateDepartementPayload payload) {
        Departement departement = Departement.builder()
                .nom(payload.getNom())
                .adresse(payload.getAdresse())
                .build();
        return departementRepository.save(departement);
    }

    // Lister tous les départements
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }

    // Trouver un département par ID
    public Departement getDepartementById(int idDepartement) {
        return departementRepository.findById(idDepartement)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));
    }

    // Mettre à jour un département
    public Departement updateDepartement(int idDepartement, CreateDepartementPayload payload) {
        Departement departement = departementRepository.findById(idDepartement)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));
        departement.setNom(payload.getNom());
        departement.setAdresse(payload.getAdresse());
        return departementRepository.save(departement);
    }

    // Supprimer un département
    public void deleteDepartement(int idDepartement) {
        departementRepository.deleteById(idDepartement);
    }

    // Méthode exécutée au démarrage de l'application
    @Override
    public void run(String... args) throws Exception {
        // Vérifier s'il n'y a pas déjà des départements dans la base de données
        if (departementRepository.count() == 0) {
            // Liste de 10 départements par défaut (5 tech + 5 agro)
            List<CreateDepartementPayload> departementsParDefaut = List.of(
                // Départements technologiques
                new CreateDepartementPayload("Développement Logiciel", "Bâtiment Tech A"),
                new CreateDepartementPayload("Cybersécurité", "Bâtiment Tech B"),
                new CreateDepartementPayload("Intelligence Artificielle", "Bâtiment Tech C"),
                new CreateDepartementPayload("Infrastructure IT", "Bâtiment Tech D"),
                new CreateDepartementPayload("Data Science", "Bâtiment Tech E"),
                // Départements agroalimentaires
                new CreateDepartementPayload("Production Agricole", "Ferme A1"),
                new CreateDepartementPayload("Transformation Alimentaire", "Usine B1"),
                new CreateDepartementPayload("Recherche Agronomique", "Labo C1"),
                new CreateDepartementPayload("Contrôle Qualité Agro", "Bâtiment D1"),
                new CreateDepartementPayload("Distribution Alimentaire", "Entrepôt E1")
            );

            // Créer chaque département dans la base de données
            for (CreateDepartementPayload payload : departementsParDefaut) {
                createDepartement(payload);
            }
            System.out.println("10 départements par défaut ont été créés avec succès.");
        }
    }
}