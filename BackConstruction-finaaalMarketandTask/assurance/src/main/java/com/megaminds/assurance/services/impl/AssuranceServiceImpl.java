package com.megaminds.assurance.services.impl;

import com.megaminds.assurance.entities.Assurance;
import com.megaminds.assurance.repositories.AssuranceRepository;
import com.megaminds.assurance.services.AssuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssuranceServiceImpl implements AssuranceService {

    @Autowired
    private AssuranceRepository assuranceRepository;

    @Override
    public Assurance createAssurance(Assurance assurance) {
        return assuranceRepository.save(assurance);
    }

    @Override
    public Optional<Assurance> getAssuranceById(Long id) {
        return assuranceRepository.findById(id);
    }

    @Override
    public List<Assurance> getAllAssurances() {
        return assuranceRepository.findAll();
    }

    @Override
    public Assurance updateAssurance(Long id, Assurance assurance) {
        if (assuranceRepository.existsById(id)) {
            assurance.setId(id);
            return assuranceRepository.save(assurance);
        }
        return null;
    }

    @Override
    public void deleteAssurance(Long id) {
        assuranceRepository.deleteById(id);
    }
}
