package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.Entities.Reclamation;
import com.PIDEV.Communication_Service.Entities.User;
import com.PIDEV.Communication_Service.Repositories.ReclamationRepository;
import com.PIDEV.Communication_Service.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ReclamationServiceImpl implements IReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private EmailService emailService; // Injection du service email

    @Override
    public Reclamation addReclamation(Reclamation reclamation, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        reclamation.setUser(user);
        reclamation.setDateReclamation(java.time.LocalDateTime.now());
        Reclamation savedReclamation = reclamationRepository.save(reclamation);

        // Envoi des emails après création de la réclamation
        sendReclamationEmails(savedReclamation, user);

        return savedReclamation;
    }

    private void sendReclamationEmails(Reclamation reclamation, User user) {
        try {
            // Email à l'administrateur
            String adminEmail = "ahmeddslama2002@gmail.com";
            String adminSubject = "Nouvelle réclamation #" + reclamation.getIdreclamation();
            String adminText = "Une nouvelle réclamation a été créée:\n\n"
                    + "Type: " + reclamation.getType() + "\n"
                    + "Titre: " + reclamation.getTitre() + "\n"
                    + "Description: " + reclamation.getDescription() + "\n"
                    + "Utilisateur: " + user.getUsername() + " " + user.getUsername() + "\n"
                    + "Date: " + reclamation.getDateReclamation();

            emailService.sendSimpleEmail(adminEmail, adminSubject, adminText);

            // Email de confirmation à l'utilisateur
            String userEmail = user.getEmail(); // Supposons que User a un champ email
            String userSubject = "Confirmation de votre réclamation #" + reclamation.getIdreclamation();
            String userText = "Bonjour " + user.getUsername() + ",\n\n"
                    + "Nous avons bien reçu votre réclamation avec les détails suivants:\n\n"
                    + "Type: " + reclamation.getType() + "\n"
                    + "Titre: " + reclamation.getTitre() + "\n"
                    + "Description: " + reclamation.getDescription() + "\n\n"
                    + "Nous traiterons votre demande dans les plus brefs délais.\n\n"
                    + "Cordialement,\nL'équipe de support";

            if (userEmail != null && !userEmail.isEmpty()) {
                emailService.sendSimpleEmail(userEmail, userSubject, userText);
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi des emails: " + e.getMessage());
            // Vous pouvez choisir de logger l'erreur sans interrompre le flux
        }
    }
    @Override
    public Reclamation updateReclamation(Long id, Map<String, Object> updates, Long userId) {
        Reclamation existingReclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));

        if (!existingReclamation.getUser().getIduser().equals(userId)) {
            throw new RuntimeException("Vous ne pouvez modifier que vos propres réclamations !");
        }

        // Mettre à jour uniquement les champs fournis
        if (updates.containsKey("titre")) {
            existingReclamation.setTitre((String) updates.get("titre"));
        }
        if (updates.containsKey("description")) {
            existingReclamation.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("type")) {
            existingReclamation.setType((String) updates.get("type"));
        }
        if (updates.containsKey("status")) {
            existingReclamation.setStatus((String) updates.get("status"));
        }

        return reclamationRepository.save(existingReclamation);
    }

    @Override
    public void deleteReclamation(Long id) {
        // Trouver la réclamation par son ID
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("Réclamation non trouvée avec l'ID : " + id);
                    return new RuntimeException("Réclamation non trouvée");
                });
//
//        // Vérifier si l'utilisateur est le propriétaire de la réclamation
//        if (!reclamation.getUser().getIduser().equals(userId)) {
//            System.err.println("Tentative de suppression par un utilisateur non autorisé. ID utilisateur : " + userId);
//            throw new RuntimeException("Vous ne pouvez supprimer que vos propres réclamations !");
//        }

        // Supprimer la réclamation
        reclamationRepository.deleteById(id);
        System.out.println("Réclamation supprimée avec succès. ID : " + id);
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public List<Reclamation> getReclamationsByUser(Long userId) {
        return reclamationRepository.findAll().stream()
                .filter(reclamation -> reclamation.getUser() != null && reclamation.getUser().getIduser().equals(userId))
                .toList();
    }


    @Override
    public Reclamation getReclamationById(Long id) {
        return reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));
    }
}
