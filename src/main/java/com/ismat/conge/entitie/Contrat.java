package com.ismat.conge.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contrat")
@Builder
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contrat")
    private int idContrat;

    @Column(name = "numero_contrat", nullable = false, unique = true)
    private String numeroContrat;

    @Column(name = "date_contrat", nullable = false)
    private LocalDate dateContrat;

    @Column(name = "duree_contrat")
    private Time dureeContrat;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_contrat", nullable = false)
    private TypeContrat typeContrat;

    @OneToOne
    @JoinColumn(name = "id_personnel", nullable = false)
    private Personnel personnel;

    public enum TypeContrat {
        CD("Contrat à Durée Déterminée"), CDI("Contrat à Durée Indéterminée");

        private final String displayName;

        TypeContrat(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @PrePersist
    protected void onCreate() {
        if (this.dateContrat == null) {
            this.dateContrat = LocalDate.now();
        }
    }
}