package com.megaminds.assurance.controllers;

import com.megaminds.assurance.entities.Projet;
import com.megaminds.assurance.repositories.ProjetRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/assurance/projets")
public class ProjetController {
    private final ProjetRepository projetRepository;

    public ProjetController(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    @GetMapping
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }
}
