package com.ismat.conge.payload.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCongePayload {
    @NotNull(message = "La durée maximale est requise")
    private Integer dureeMax; // Changé en Integer pour correspondre à l'entité

    @NotNull(message = "Le type de congé est requis")
    private String type;

    @NotNull(message = "L'ID du personnel est requis")
    private int idPersonnel; // Changé en int (non nullable)
}