package com.ismat.conge.payload.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePersonnelPayload {
    private Integer idPersonnel;
    
    @NotBlank(message = "Le nom est requis")
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    private String prenom;

    private String adresse;

    @NotNull(message = "La date de naissance est requise")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;

    private String telephone;

    @NotBlank(message = "L'email est requis")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;

    @NotNull(message = "L'ID du département est requis")
    private Integer idDepartement; // Champ nommé "idDepartement" pour correspondre au getter attendu

    public CreatePersonnelPayload(String nom, String prenom, String adresse, LocalDate dateNaissance, String telephone, String email, String password, int idDepartement) {
    }
}