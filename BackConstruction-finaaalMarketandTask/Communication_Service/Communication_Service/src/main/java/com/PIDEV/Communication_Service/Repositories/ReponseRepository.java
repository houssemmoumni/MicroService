package com.PIDEV.Communication_Service.Repositories;

import com.PIDEV.Communication_Service.Entities.Reclamation;
import com.PIDEV.Communication_Service.Entities.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
//    List<Reponse> findByReclamation(Reclamation reclamation);
//    List<Reponse> findByReclamation(Reclamation reclamation);
List<Reponse> findByReclamation(Reclamation reclamation);

}
