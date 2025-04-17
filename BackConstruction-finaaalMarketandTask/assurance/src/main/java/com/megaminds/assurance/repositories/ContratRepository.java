package com.megaminds.assurance.repositories;

import com.megaminds.assurance.entities.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {
    // You can add custom queries here if needed.
}
