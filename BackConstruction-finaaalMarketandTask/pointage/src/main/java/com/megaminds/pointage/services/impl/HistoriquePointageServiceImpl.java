package com.megaminds.pointage.services.impl;

import com.megaminds.pointage.entities.HistoriquePointage;
import com.megaminds.pointage.repositories.HistoriquePointageRepository;
import com.megaminds.pointage.services.HistoriquePointageService;
import com.megaminds.pointage.services.WhatsappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
public class HistoriquePointageServiceImpl implements HistoriquePointageService {

    @Autowired
    private HistoriquePointageRepository historiquePointageRepository;

    @Autowired
    private WhatsappService whatsappService;

    @Override
    public HistoriquePointage getHistoriquePointageById(Long id) {
        return historiquePointageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HistoriquePointage not found with id " + id));
    }

    @Override
    public List<HistoriquePointage> getAllHistoriquePointages() {
        return historiquePointageRepository.findAll();
    }

    @Override
    public HistoriquePointage createHistoriquePointage(HistoriquePointage historiquePointage) {
        validateDates(historiquePointage);
        historiquePointage.setScore(calculateScore(historiquePointage));
        HistoriquePointage saved = historiquePointageRepository.save(historiquePointage);

        //sendWhatsappNotification(saved);
        return saved;
    }

    @Override
    public HistoriquePointage updateHistoriquePointage(Long id, HistoriquePointage historiquePointage) {
        return historiquePointageRepository.findById(id).map(existing -> {
            if (historiquePointage.getJourPointage() != null)
                existing.setJourPointage(historiquePointage.getJourPointage());
            if (historiquePointage.getTempsEntree() != null)
                existing.setTempsEntree(historiquePointage.getTempsEntree());
            if (historiquePointage.getTempsSortie() != null)
                existing.setTempsSortie(historiquePointage.getTempsSortie());
            if (historiquePointage.getLocalisation() != null)
                existing.setLocalisation(historiquePointage.getLocalisation());
            if (historiquePointage.getUser() != null)
                existing.setUser(historiquePointage.getUser());
            if (historiquePointage.getTempsCommencement() != null)
                existing.setTempsCommencement(historiquePointage.getTempsCommencement());
            if (historiquePointage.getTempsFinition() != null)
                existing.setTempsFinition(historiquePointage.getTempsFinition());

            validateDates(existing);
            existing.setScore(calculateScore(existing));

            return historiquePointageRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("HistoriquePointage not found with id " + id));
    }

    @Override
    public void deleteHistoriquePointage(Long id) {
        if (!historiquePointageRepository.existsById(id)) {
            throw new RuntimeException("HistoriquePointage not found with id " + id);
        }
        historiquePointageRepository.deleteById(id);
    }

    private void validateDates(HistoriquePointage historiquePointage) {
        LocalTime tempsEntree = historiquePointage.getTempsEntree();
        LocalTime tempsSortie = historiquePointage.getTempsSortie();
        LocalTime tempsCommencement = historiquePointage.getTempsCommencement();
        LocalTime tempsFinition = historiquePointage.getTempsFinition();

        if (tempsEntree == null || tempsSortie == null || tempsCommencement == null || tempsFinition == null) {
            throw new IllegalArgumentException("Toutes les dates doivent être renseignées.");
        }

        if (tempsEntree.isBefore(tempsCommencement) || tempsEntree.isAfter(tempsSortie)) {
            throw new IllegalArgumentException("L'heure d'entrée doit être entre l'heure de commencement et l'heure de sortie.");
        }

        if (tempsSortie.isBefore(tempsEntree) || tempsSortie.isAfter(tempsFinition)) {
            throw new IllegalArgumentException("L'heure de sortie doit être entre l'heure d'entrée et l'heure de finition.");
        }
    }

    private int calculateScore(HistoriquePointage historiquePointage) {
        LocalTime entree = historiquePointage.getTempsEntree();
        LocalTime sortie = historiquePointage.getTempsSortie();
        LocalTime commencement = historiquePointage.getTempsCommencement();
        LocalTime finition = historiquePointage.getTempsFinition();

        int score = 100;
        int penaltyPer3Min = 1;

        long late = Duration.between(commencement, entree).toMinutes();
        if (late > 0) score -= (late / 3) * penaltyPer3Min;

        long earlyLeave = Duration.between(sortie, finition).toMinutes();
        if (earlyLeave > 0) score -= (earlyLeave / 3) * penaltyPer3Min;

        return Math.max(score, 0);
    }

    private void sendWhatsappNotification(HistoriquePointage historiquePointage) {
        if (historiquePointage.getUser() == null || historiquePointage.getUser().getTelephone() == null) {
            return;
        }

        String message = String.format("✅ Pointage confirmé le %s à %s. Score: %d/100",
                historiquePointage.getJourPointage(),
                historiquePointage.getTempsEntree(),
                historiquePointage.getScore());

        String formattedPhone = "+".concat(String.valueOf(historiquePointage.getUser().getTelephone()));
        whatsappService.sendMessage(formattedPhone, message);
    }
}
