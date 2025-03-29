package com.ismat.conge.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conge")
@Builder
public class Conge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conge")
    private int idConge;

    @Column(name = "duree_max") // Nombre de jours
    private Integer dureeMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    @Builder.Default
    private Statut statut = Statut.EN_ATTENTE;

    @ManyToOne
    @JoinColumn(name = "id_personnel", nullable = false)
    private Personnel personnel;

    public enum Type {
        PAYER("payé"), NON_PAYER("non payé");

        private final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Statut {
        EN_ATTENTE, ACCEPTE, REJETE
    }
}