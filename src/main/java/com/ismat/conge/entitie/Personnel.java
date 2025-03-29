package com.ismat.conge.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personnel")
@Builder
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personnel")
    private int idPersonnel;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_departement", nullable = false)
    private Departement departement;

    @OneToMany(mappedBy = "personnel", cascade = CascadeType.ALL)
    private List<Conge> conges;

    @OneToOne(mappedBy = "personnel", cascade = CascadeType.ALL)
    private Contrat contrat;
}