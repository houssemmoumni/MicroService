package com.megaminds.pointage.repositories;

import com.megaminds.pointage.entities.HistoriquePointage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriquePointageRepository extends JpaRepository<HistoriquePointage, Long> {
    // Vous pouvez ajouter des méthodes de recherche personnalisées ici si nécessaire
}
