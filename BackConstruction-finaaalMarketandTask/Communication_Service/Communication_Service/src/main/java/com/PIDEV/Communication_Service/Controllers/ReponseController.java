package com.PIDEV.Communication_Service.Controllers;

import com.PIDEV.Communication_Service.Entities.Reclamation;
import com.PIDEV.Communication_Service.Entities.Reponse;
import com.PIDEV.Communication_Service.Services.IReponseService;
import com.PIDEV.Communication_Service.Services.TwilioService;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/responses")

@RequiredArgsConstructor
public class ReponseController {

    @Autowired
    private  IReponseService reponseService;

   @Autowired private TwilioService twilioService;

    @PostMapping("/addResponse")
    public ResponseEntity<Reponse> addResponse(
            @RequestBody Reponse reponse,
            @RequestParam("reclamationId") Long reclamationId,
            @RequestParam("userId") Long userId) {

        Reponse savedReponse = reponseService.addReponse(reponse, reclamationId, userId);
        return ResponseEntity.ok(savedReponse);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReponse(
            @PathVariable Long id,
            @RequestBody Reponse reponse,
            @RequestParam Long userId) {

        try {
            Reponse updatedReponse = reponseService.updateReponse(id, reponse, userId);
            return ResponseEntity.ok(updatedReponse);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La réclamation associée ou l'utilisateur est introuvable");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<Void> deleteResponse(
            @PathVariable Long id,
            @PathVariable Long userId) {
        try {
            reponseService.deleteReponse(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reponse>> getAllResponses() {
        return ResponseEntity.ok(reponseService.getAllReponses());
    }

    @GetMapping("/reclamation/{reclamationId}")
    public List<Reponse> getReponsesByReclamation(@PathVariable Long reclamationId) {
        return reponseService.getReponsesByReclamation(reclamationId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reponse> getResponseById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(reponseService.getReponseById(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}