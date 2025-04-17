package com.PIDEV.Communication_Service.Services;

import com.PIDEV.Communication_Service.Entities.Reclamation;
import java.util.List;
import java.util.Map;

public interface IReclamationService {
    Reclamation addReclamation(Reclamation reclamation, Long userId);
    //Reclamation updateReclamation(Long id, Reclamation reclamation, Long userId);

    Reclamation updateReclamation(Long id, Map<String, Object> updates, Long userId);



    void deleteReclamation(Long id);
    List<Reclamation> getAllReclamations();
    List<Reclamation> getReclamationsByUser(Long userId);
    Reclamation getReclamationById(Long id);
}
