package com.megaminds.assurance.repositories;

import com.megaminds.assurance.entities.Maintenance;
import com.megaminds.assurance.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
