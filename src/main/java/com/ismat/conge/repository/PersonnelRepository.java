package com.ismat.conge.repository;

import com.ismat.conge.entitie.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonnelRepository extends JpaRepository<Personnel, Integer> {
    List<Personnel> findByDepartementIdDepartement(int idDepartement);

    Optional<Personnel> findByEmail(String email);
}