package com.megaminds.pointage.services;

import com.megaminds.pointage.entities.HistoriquePointage;
import java.util.List;

public interface HistoriquePointageService {
    HistoriquePointage getHistoriquePointageById(Long id);
    List<HistoriquePointage> getAllHistoriquePointages();
    HistoriquePointage createHistoriquePointage(HistoriquePointage historiquePointage);
    HistoriquePointage updateHistoriquePointage(Long id, HistoriquePointage historiquePointage);
    void deleteHistoriquePointage(Long id);
}
