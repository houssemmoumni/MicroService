package com.megaminds.assurance.controllers;

import com.megaminds.assurance.entities.Contrat;
import com.megaminds.assurance.services.ContratService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/assurance/contrats")
public class ContratController {

    @Autowired
    private ContratService contratService;

    // Créer un nouveau contrat
    @PostMapping
    public ResponseEntity<Contrat> createContrat(@RequestBody Contrat contrat) {
        try {
            Contrat createdContrat = contratService.createContrat(contrat);
            return new ResponseEntity<>(createdContrat, HttpStatus.CREATED);  // Retour avec le statut 201
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // Retour en cas d'erreur
        }
    }

    // Récupérer un contrat par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Contrat> getContratById(@PathVariable Long id) {
        Optional<Contrat> contrat = contratService.getContratById(id);
        return contrat.map(value -> new ResponseEntity<>(value, HttpStatus.OK))  // Si trouvé, retour OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));  // Si non trouvé, retour 404
    }

    // Récupérer tous les contrats
    @GetMapping
    public ResponseEntity<List<Contrat>> getAllContrats() {
        List<Contrat> contrats = contratService.getAllContrats();
        return new ResponseEntity<>(contrats, HttpStatus.OK);  // Retour avec tous les contrats
    }

    // Mettre à jour un contrat existant
    @PutMapping("/{id}")
    public ResponseEntity<Contrat> updateContrat(@PathVariable Long id, @RequestBody Contrat contrat) {
        try {
            Contrat updatedContrat = contratService.updateContrat(id, contrat);
            return new ResponseEntity<>(updatedContrat, HttpStatus.OK);  // Retour avec le contrat mis à jour
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // Si le contrat n'existe pas, retour 404
        }
    }

    // Supprimer un contrat par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContrat(@PathVariable Long id) {
        try {
            contratService.deleteContrat(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Retour 204 si la suppression réussie
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Retour 404 si le contrat n'existe pas
        }
    }
}
