package com.megaminds.assurance.repositories;

import com.megaminds.assurance.entities.Assurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssuranceRepository extends JpaRepository<Assurance, Long> {
    // You can add custom queries here if needed
}
