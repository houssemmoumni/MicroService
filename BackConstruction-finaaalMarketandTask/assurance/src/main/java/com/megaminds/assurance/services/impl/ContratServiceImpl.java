package com.megaminds.assurance.services.impl;

import com.megaminds.assurance.entities.Assurance;
import com.megaminds.assurance.entities.Contrat;
import com.megaminds.assurance.entities.Projet;
import com.megaminds.assurance.repositories.AssuranceRepository;
import com.megaminds.assurance.repositories.ContratRepository;
import com.megaminds.assurance.repositories.ProjetRepository;
import com.megaminds.assurance.services.ContratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContratServiceImpl implements ContratService {

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private AssuranceRepository assuranceRepository; // Ajout du repository pour Assurance

    @Override
    public Contrat createContrat(Contrat contrat) {
        // Vérifier et récupérer le Projet existant
        if (contrat.getProjet() != null && contrat.getProjet().getId() != null) {
            Optional<Projet> projetOpt = projetRepository.findById(contrat.getProjet().getId());
            projetOpt.ifPresent(contrat::setProjet);
        }

        // Vérifier et récupérer l'Assurance existante
        if (contrat.getAssurance() != null && contrat.getAssurance().getId() != null) {
            Optional<Assurance> assuranceOpt = assuranceRepository.findById(contrat.getAssurance().getId());
            assuranceOpt.ifPresent(contrat::setAssurance);
        }

        // Enregistrer le contrat
        return contratRepository.save(contrat);
    }

    @Override
    public Optional<Contrat> getContratById(Long id) {
        return contratRepository.findById(id);
    }

    @Override
    public List<Contrat> getAllContrats() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat updateContrat(Long id, Contrat contrat) {
        return contratRepository.findById(id).map(existingContrat -> {
            existingContrat.setContratcondition(contrat.getContratcondition());
            existingContrat.setDate_signature(contrat.getDate_signature());
            existingContrat.setDate_expiration(contrat.getDate_expiration());

            // Vérifier et mettre à jour l'Assurance existante
            if (contrat.getAssurance() != null && contrat.getAssurance().getId() != null) {
                Optional<Assurance> assuranceOpt = assuranceRepository.findById(contrat.getAssurance().getId());
                assuranceOpt.ifPresent(existingContrat::setAssurance);
            }

            // Vérifier et mettre à jour le Projet existant
            if (contrat.getProjet() != null && contrat.getProjet().getId() != null) {
                Optional<Projet> projetOpt = projetRepository.findById(contrat.getProjet().getId());
                projetOpt.ifPresent(existingContrat::setProjet);
            }

            return contratRepository.save(existingContrat);
        }).orElseThrow(() -> new RuntimeException("Contrat not found with id " + id));
    }

    @Override
    public void deleteContrat(Long id) {
        contratRepository.deleteById(id);
    }
}