package com.megaminds.incident.repository;

import com.megaminds.incident.entity.IncidentAction;
import com.megaminds.incident.entity.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IncidentActionRepository extends JpaRepository<IncidentAction, Long> {
    List<IncidentAction> findByIncidentReportIdOrderByActionDateDesc(Long incidentId);

}
