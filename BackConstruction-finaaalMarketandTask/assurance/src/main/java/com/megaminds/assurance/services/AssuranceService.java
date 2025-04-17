package com.megaminds.assurance.services;

import com.megaminds.assurance.entities.Assurance;
import java.util.List;
import java.util.Optional;

public interface AssuranceService {
    Assurance createAssurance(Assurance assurance);
    Optional<Assurance> getAssuranceById(Long id);
    List<Assurance> getAllAssurances();
    Assurance updateAssurance(Long id, Assurance assurance);
    void deleteAssurance(Long id);
}
