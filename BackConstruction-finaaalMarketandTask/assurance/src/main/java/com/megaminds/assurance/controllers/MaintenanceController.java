package com.megaminds.assurance.controllers;

import com.megaminds.assurance.entities.Maintenance;
import com.megaminds.assurance.entities.MaintenanceStatus;
import com.megaminds.assurance.services.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/assurance/maintenances")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    // Inject MaintenanceService only (EmailService is already handled in MaintenanceServiceImpl)
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    // ✅ Créer une nouvelle maintenance
    @PostMapping
    public ResponseEntity<Maintenance> createMaintenance(@RequestBody Maintenance maintenance) {
        Maintenance savedMaintenance = maintenanceService.createMaintenance(maintenance);
        return ResponseEntity.ok(savedMaintenance);
    }

    // ✅ Récupérer une maintenance par ID
    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Long id) {
        Optional<Maintenance> maintenance = maintenanceService.getMaintenanceById(id);
        return maintenance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Récupérer toutes les maintenances
    @GetMapping
    public List<Maintenance> getAllMaintenances() {
        return maintenanceService.getAllMaintenances();
    }

    // ✅ Modifier une maintenance
    @PutMapping("/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Long id, @RequestBody Maintenance maintenance) {
        Maintenance updatedMaintenance = maintenanceService.updateMaintenance(id, maintenance);
        return updatedMaintenance != null ? ResponseEntity.ok(updatedMaintenance) : ResponseEntity.notFound().build();
    }

    // ✅ Supprimer une maintenance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Mettre à jour le statut uniquement (pas besoin d'envoyer un email ici, car c'est déjà géré dans le service)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateMaintenanceStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");

        Optional<Maintenance> maintenanceOpt = maintenanceService.getMaintenanceById(id);
        if (maintenanceOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Maintenance introuvable.");
        }

        try {
            Maintenance maintenance = maintenanceOpt.get();
            maintenance.setStatus(MaintenanceStatus.valueOf(newStatus.toUpperCase())); // Convertir en Enum
            maintenanceService.updateMaintenance(id, maintenance);

            return ResponseEntity.ok(Map.of("message", "Statut mis à jour avec succès !"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Statut de maintenance invalide : " + newStatus);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la mise à jour du statut.");
        }
    }
}