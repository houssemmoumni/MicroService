package com.PIDEV.Communication_Service.Controllers;

import com.PIDEV.Communication_Service.DTO.AdminNotification;
import com.PIDEV.Communication_Service.Entities.Reclamation;
import com.PIDEV.Communication_Service.Entities.Reponse;

import com.PIDEV.Communication_Service.Repositories.ReclamationRepository;
import com.PIDEV.Communication_Service.Services.IReclamationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reclamations")
@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "*")// Ajoute cette annotation si besoin
public class ReclamationController {

    @Autowired
    private IReclamationService reclamationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ReclamationRepository reclamationRepository;

//    @Autowired
//    private IReponseService reponseService;

    // Ajouter une réclamation (Client et Ouvrier)



    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouterReclamation(
            @RequestParam Long userId,
            @RequestParam String titre,
            @RequestParam String description,
            @RequestParam String type) {

        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(titre);
        reclamation.setDescription(description);
        reclamation.setType(type);

        Reclamation saved = reclamationService.addReclamation(reclamation, userId);
        return ResponseEntity.ok(saved);
    }



    // Récupérer toutes les réclamations (Admin seulement)
    @GetMapping("/all")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    // Récupérer une réclamation par ID (Accessible par tous)
    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.getReclamationById(id));
    }

    // Mettre à jour une réclamation (Seulement par son propriétaire : Client ou Ouvrier)
    @PutMapping(value = "/modifier/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reclamation> modifierReclamation(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,  // ← Accepte un Map au lieu de Reclamation
            @RequestParam Long userId
    ) {
        Reclamation updatedReclamation = reclamationService.updateReclamation(id, updates, userId);
        return ResponseEntity.ok(updatedReclamation);
    }


    // Supprimer une réclamation (Seulement par son propriétaire)
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> supprimerReclamation(@PathVariable Long id
                                                     ) {
        System.out.println("Requête de suppression reçue. ID réclamation : " + id );
        reclamationService.deleteReclamation(id);
        return ResponseEntity.ok().build();
    }


    // Récupérer les réclamations par userId (Accessible par tous)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reclamation>> getReclamationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reclamationService.getReclamationsByUser(userId));
    }


    @Scheduled(fixedRate = 10000)
    public void checkPendingReclamations() {
        List<Reclamation> pendingRecs = reclamationRepository.findByStatus("en attente");

        if (!pendingRecs.isEmpty()) {
            Map<String, Object> notification = new HashMap<>();
            notification.put("message", "Réclamations en attente: " + pendingRecs.size());
            notification.put("count", pendingRecs.size());
            notification.put("timestamp", LocalDateTime.now());

            messagingTemplate.convertAndSend("/topic/admin-notifications", notification);
        }
    }

    // Endpoint pour tester manuellement
    @GetMapping("/test-ws")
    public ResponseEntity<String> testWebSocket() {
        Map<String, Object> testMsg = new HashMap<>();
        testMsg.put("message", "Ceci est un test");
        testMsg.put("count", 1);

        messagingTemplate.convertAndSend("/topic/admin-notifications", testMsg);
        return ResponseEntity.ok("Message de test envoyé");
    }



//    // Ajouter une réponse à une réclamation (Seulement par l'Admin)
//    @PostMapping("/{id}/reponse")
//    public ResponseEntity<Reponse> ajouterReponse(@PathVariable Long id, @RequestBody Reponse reponse) {
//        return ResponseEntity.ok(reponseService.ajouterReponse(id, reponse));
//    }
//
//    // Récupérer toutes les réponses d'une réclamation (Accessibles par tous)
//    @GetMapping("/{id}/reponses")
//    public ResponseEntity<List<Reponse>> getReponsesByReclamation(@PathVariable Long id) {
//        return ResponseEntity.ok(reponseService.getReponsesByReclamation(id));
//    }
}
