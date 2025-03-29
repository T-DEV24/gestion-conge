package com.ismat.conge.payload.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateContratPayload {
    @NotBlank(message = "Le numéro du contrat est requis")
    private String numeroContrat;

    @NotNull(message = "La date du contrat est requise")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateContrat; // Ajout pour correspondre à Contrat

    @NotNull(message = "La durée du contrat est requise")
    private Time dureeContrat;

    @NotBlank(message = "Le type de contrat est requis")
    private String typeContrat;

    @NotNull(message = "L'ID du personnel est requis")
    private Integer idPersonnel;


    public CreateContratPayload(String numeroContrat, Time dureeContrat, String name, int idPersonnel) {
    }
}