package com.ismat.conge.repository;

import com.ismat.conge.entitie.Conge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CongeRepository extends JpaRepository<Conge, Integer> {
    List<Conge> findByPersonnelIdPersonnel(int idPersonnel);
    long countByType(Conge.Type type);
    long countByPersonnelDepartementIdDepartement(int idDepartement);
}