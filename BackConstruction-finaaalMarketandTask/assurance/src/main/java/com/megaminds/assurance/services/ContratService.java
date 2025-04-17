package com.megaminds.assurance.services;

import com.megaminds.assurance.entities.Contrat;

import java.util.List;
import java.util.Optional;

public interface ContratService {

    Contrat createContrat(Contrat contrat);

    Optional<Contrat> getContratById(Long id);

    List<Contrat> getAllContrats();

    Contrat updateContrat(Long id, Contrat contrat);

    void deleteContrat(Long id);
}
