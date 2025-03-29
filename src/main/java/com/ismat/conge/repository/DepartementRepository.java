package com.ismat.conge.repository;

import com.ismat.conge.entitie.Departement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartementRepository extends JpaRepository<Departement, Integer> {
}