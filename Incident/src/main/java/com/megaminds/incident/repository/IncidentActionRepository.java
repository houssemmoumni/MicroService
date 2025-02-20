package com.megaminds.incident.repository;

import com.megaminds.incident.entity.IncidentAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentActionRepository extends JpaRepository<IncidentAction, Long> {
}
