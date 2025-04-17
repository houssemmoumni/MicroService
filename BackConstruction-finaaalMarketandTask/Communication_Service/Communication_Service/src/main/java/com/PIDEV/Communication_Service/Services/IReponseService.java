package com.PIDEV.Communication_Service.Services;
import java.util.List;
import com.PIDEV.Communication_Service.Entities.Reponse;

public interface IReponseService {
    Reponse addReponse(Reponse reponse, Long reclamationId, Long userId);
    Reponse updateReponse(Long id, Reponse reponse, Long userId);
    void deleteReponse(Long id, Long userId);
    List<Reponse> getAllReponses();
//    List<Reponse> getReponsesByReclamation(Long reclamationId);
    Reponse getReponseById(Long id);

    List<Reponse> getReponsesByReclamation(Long reclamationId);
}

