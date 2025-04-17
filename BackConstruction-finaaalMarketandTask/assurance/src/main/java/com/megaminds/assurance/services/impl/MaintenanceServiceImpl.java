package com.megaminds.assurance.services.impl;

import com.megaminds.assurance.entities.Contrat;
import com.megaminds.assurance.entities.Maintenance;
import com.megaminds.assurance.entities.MaintenanceStatus;
import com.megaminds.assurance.entities.MaintenancePriority; // Nouvel enum pour la priorité
import com.megaminds.assurance.repositories.ContratRepository;
import com.megaminds.assurance.repositories.MaintenanceRepository;
import com.megaminds.assurance.services.EmailService;
import com.megaminds.assurance.services.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private EmailService emailService;

    // Définir les ensembles de mots-clés pour la priorité
    private final Set<String> highPriorityKeywords = new HashSet<>(Arrays.asList(
            "urgent", "critique", "immédiat", "panne", "bloquant",
            "sécurité", "risque", "légal", "financier",
            "production", "client important", "danger", "catastrophe",
            "error in course", "bug in the site", "bad words in forum",
            "the payment doesn't work", "the payment"
    ));

    private final Set<String> mediumPriorityKeywords = new HashSet<>(Arrays.asList(
            "problème", "dysfonctionnement", "amélioration",
            "demande", "incompréhension", "confusion",
            "délai", "retard", "impact", "significatif"
    ));

    private final Set<String> lowPriorityKeywords = new HashSet<>(Arrays.asList(
            "information", "suggestion", "conseil", "question",
            "curiosité", "non urgent", "faible impact", "secondaire",
            "optionnel", "exploratoire", "minimal", "trivial", "advice"
    ));

    @Override
    public Maintenance createMaintenance(Maintenance maintenance) {
        if (maintenance.getContrat() != null && maintenance.getContrat().getId() != null) {
            Optional<Contrat> contratOpt = contratRepository.findById(maintenance.getContrat().getId());
            if (contratOpt.isPresent()) {
                maintenance.setContrat(contratOpt.get());
            } else {
                throw new RuntimeException("Contrat avec ID " + maintenance.getContrat().getId() + " non trouvé.");
            }
        }

        // Vérifier et définir le statut en fonction de la date d'expiration du contrat
        maintenance.checkAndSetStatus();

        // Classer la priorité en fonction des mots-clés dans la description
        MaintenancePriority priority = classifyMaintenancePriority(maintenance);
        maintenance.setPriority(priority); // Définir la priorité

        // Enregistrer la maintenance dans la base de données
        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);

        // Envoyer un e-mail avec le statut de la maintenance (sans la priorité)
        emailService.sendStatusChangeEmail(
                savedMaintenance.getTitle(), // Titre de la maintenance
                savedMaintenance.getStatus().name(), // Statut de la maintenance
                savedMaintenance.getEmail() // Adresse e-mail du destinataire
        );

        return savedMaintenance;
    }

    /**
     * Classer la priorité de la maintenance en fonction des mots-clés dans la description.
     *
     * @param maintenance La maintenance à classifier.
     * @return La priorité de la maintenance.
     */
    private MaintenancePriority classifyMaintenancePriority(Maintenance maintenance) {
        String description = maintenance.getDescription().toLowerCase(); // Convertir en minuscules

        // Compter les occurrences des mots-clés
        int highPriorityScore = countKeywords(description, highPriorityKeywords);
        int mediumPriorityScore = countKeywords(description, mediumPriorityKeywords);
        int lowPriorityScore = countKeywords(description, lowPriorityKeywords);

        // Déterminer la priorité en fonction des scores
        if (highPriorityScore >= 2) {
            return MaintenancePriority.HIGH;
        } else if (mediumPriorityScore >= 2 || highPriorityScore == 1) {
            return MaintenancePriority.MEDIUM;
        } else if (lowPriorityScore >= 1) {
            return MaintenancePriority.LOW;
        } else {
            return MaintenancePriority.MEDIUM; // Par défaut, priorité moyenne
        }
    }

    /**
     * Compter le nombre de mots-clés présents dans la description.
     *
     * @param description La description de la maintenance.
     * @param keywords   L'ensemble des mots-clés à rechercher.
     * @return Le nombre de mots-clés trouvés.
     */
    private int countKeywords(String description, Set<String> keywords) {
        int count = 0;
        for (String keyword : keywords) {
            if (description.contains(keyword)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Optional<Maintenance> getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id);
    }

    @Override
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance updateMaintenance(Long id, Maintenance maintenance) {
        Optional<Maintenance> existingMaintenanceOpt = maintenanceRepository.findById(id);

        if (existingMaintenanceOpt.isPresent()) {
            Maintenance existingMaintenance = existingMaintenanceOpt.get();

            // Sauvegarder l'ancien statut pour comparaison
            MaintenanceStatus oldStatus = existingMaintenance.getStatus();

            // Mettre à jour les champs (sauf le statut si non fourni)
            existingMaintenance.setTitle(maintenance.getTitle());
            existingMaintenance.setDescription(maintenance.getDescription());
            existingMaintenance.setImage(maintenance.getImage());
            existingMaintenance.setEmail(maintenance.getEmail());

            // Mettre à jour le statut uniquement si l'utilisateur le modifie
            if (maintenance.getStatus() != null) {
                try {
                    MaintenanceStatus status = MaintenanceStatus.valueOf(maintenance.getStatus().name());
                    existingMaintenance.setStatus(status);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Statut de maintenance invalide : " + maintenance.getStatus());
                }
            }

            // Gestion du Contrat
            if (maintenance.getContrat() != null && maintenance.getContrat().getId() != null) {
                Optional<Contrat> contratOpt = contratRepository.findById(maintenance.getContrat().getId());
                if (contratOpt.isPresent()) {
                    existingMaintenance.setContrat(contratOpt.get());
                } else {
                    throw new RuntimeException("Contrat with ID " + maintenance.getContrat().getId() + " not found.");
                }
            }

            // Enregistrer la maintenance mise à jour
            Maintenance updatedMaintenance = maintenanceRepository.save(existingMaintenance);

            // Envoyer un e-mail de notification si le statut a changé
            if (maintenance.getStatus() != null && !maintenance.getStatus().equals(oldStatus)) {
                emailService.sendStatusChangeEmail(
                        updatedMaintenance.getTitle(),
                        updatedMaintenance.getStatus().name(), // Convertir l'enum en chaîne
                        updatedMaintenance.getEmail()
                );
            }

            return updatedMaintenance;
        }

        throw new RuntimeException("Maintenance with ID " + id + " not found.");
    }

    @Override
    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }
}