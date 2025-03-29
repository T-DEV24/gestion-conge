package com.ismat.conge.repository;

import com.ismat.conge.entitie.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratRepository extends JpaRepository<Contrat, Integer> {
    Contrat findByPersonnelIdPersonnel(int idPersonnel);
}