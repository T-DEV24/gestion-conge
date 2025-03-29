package com.ismat.conge.payload.in;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepartementPayload {
    @NotBlank(message = "Le nom du département est requis")
    private String nom;

    @NotBlank
    private String adresse;
}