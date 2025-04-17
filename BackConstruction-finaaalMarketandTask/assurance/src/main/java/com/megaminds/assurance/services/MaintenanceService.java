package com.megaminds.assurance.services;

import com.megaminds.assurance.entities.Maintenance;

import java.util.List;
import java.util.Optional;

public interface MaintenanceService {
    Maintenance createMaintenance(Maintenance maintenance);
    Optional<Maintenance> getMaintenanceById(Long id);
    List<Maintenance> getAllMaintenances();
    Maintenance updateMaintenance(Long id, Maintenance maintenance);
    void deleteMaintenance(Long id);
}
