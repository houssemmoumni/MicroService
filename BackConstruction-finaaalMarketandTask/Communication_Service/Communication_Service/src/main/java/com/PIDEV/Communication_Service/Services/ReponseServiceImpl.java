package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.Entities.Reclamation;
import com.PIDEV.Communication_Service.Services.TwilioService;
import com.PIDEV.Communication_Service.Entities.Reponse;
import com.PIDEV.Communication_Service.Entities.User;
import com.PIDEV.Communication_Service.Repositories.ReclamationRepository;
import com.PIDEV.Communication_Service.Repositories.ReponseRepository;
import com.PIDEV.Communication_Service.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReponseServiceImpl implements IReponseService {

    @Autowired
    private  ReponseRepository reponseRepository;
    @Autowired
    private  ReclamationRepository reclamationRepository;
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  TwilioService twilioService;



    @Override
    public Reponse addReponse(Reponse reponse, Long reclamationId, Long userId) {
        // Trouver la réclamation
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));

        // Trouver l'utilisateur
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Associer la réponse à la réclamation et à l'utilisateur
        reponse.setReclamation(reclamation);
        reponse.setUser(user);
        reponse.setDate_reponse(LocalDate.now());

        // Mettre à jour le statut de la réclamation à "résolu"
        if (reclamation.getStatus() == null || !reclamation.getStatus().equals("résolu")) {
            reclamation.setStatus("résolu");
            reclamationRepository.save(reclamation); // Sauvegarder la réclamation mise à jour

            // Envoyer un SMS pour informer que la réclamation est traitée
            String adminPhoneNumber = "54828257"; // Numéro de l'admin
            String message = "La réclamation #" + reclamationId + " a été traitée avec succès.";
            try {
                twilioService.sendSms(adminPhoneNumber, message);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi du SMS: " + e.getMessage());
                // Vous pouvez choisir de logger l'erreur sans interrompre le processus
            }
        }

        // Sauvegarder la réponse
        return reponseRepository.save(reponse);
    }
    @Override
    public Reponse updateReponse(Long id, Reponse reponse, Long userId) {
        Reponse existingReponse = reponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Response not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify ownership
        if(!existingReponse.getReclamation().getUser().getIduser().equals(userId)) {
            throw new RuntimeException("You can only update your own responses");
        }

        existingReponse.setTitre(reponse.getTitre());
        existingReponse.setReponse(reponse.getReponse());
        existingReponse.setDate_reponse(LocalDate.now());

        return reponseRepository.save(existingReponse);
    }

    @Override
    public void deleteReponse(Long id, Long userId) {
        Reponse reponse = reponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Response not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify ownership
        if(!reponse.getReclamation().getUser().getIduser().equals(userId)) {
            throw new RuntimeException("You can only delete your own responses");
        }

        reponseRepository.delete(reponse);
    }

    @Override
    public List<Reponse> getAllReponses() {
        return reponseRepository.findAll();
    }

//    @Override
//    public List<Reponse> getReponsesByReclamation(Long reclamationId) {
//        Reclamation reclamation = reclamationRepository.findById(reclamationId)
//                .orElseThrow(() -> new RuntimeException("Reclamation not found"));
//        return reponseRepository.findByReclamation(reclamation);
//    }

    @Override
    public Reponse getReponseById(Long id) {
        return reponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Response not found"));
    }


    @Override
public List<Reponse> getReponsesByReclamation(Long reclamationId) {
    Reclamation reclamation = reclamationRepository.findById(reclamationId)
            .orElseThrow(() -> new RuntimeException("Reclamation not found"));
    return reponseRepository.findByReclamation(reclamation);
}





}