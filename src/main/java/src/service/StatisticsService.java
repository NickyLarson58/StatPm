package src.service;

import src.model.dto.StatistiqueDTO;
import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {
    List<StatistiqueDTO> getStatistiquesFiltrees(
        String brigade, 
        Integer agentId, 
        LocalDate startDate, 
        LocalDate endDate, 
        Long adresseId,
        boolean isMission
    );
}