package com.megaminds.assurance.controllers;

import com.megaminds.assurance.entities.Assurance;
import com.megaminds.assurance.services.AssuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/assurance/assurances")
public class AssuranceController {

    @Autowired
    private AssuranceService assuranceService;

    // Create a new Assurance
    @PostMapping
    public Assurance createAssurance(@RequestBody Assurance assurance) {
        return assuranceService.createAssurance(assurance);
    }

    // Get Assurance by ID
    @GetMapping("/{id}")
    public Optional<Assurance> getAssuranceById(@PathVariable Long id) {
        return assuranceService.getAssuranceById(id);
    }

    // Get all Assurances
    @GetMapping
    public List<Assurance> getAllAssurances() {
        return assuranceService.getAllAssurances();
    }

    // Update an existing Assurance
    @PutMapping("/{id}")
    public Assurance updateAssurance(@PathVariable Long id, @RequestBody Assurance assurance) {
        return assuranceService.updateAssurance(id, assurance);
    }

    // Delete an Assurance
    @DeleteMapping("/{id}")
    public void deleteAssurance(@PathVariable Long id) {
        assuranceService.deleteAssurance(id);
    }
}
