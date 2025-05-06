package src.controller;

import org.springframework.format.annotation.DateTimeFormat;
import src.model.dto.StatistiqueDTO;
import src.service.StatisticsService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistiques")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public List<StatistiqueDTO> getStatistiques(
            @RequestParam(required = false) boolean isMission,
            @RequestParam(required = false) String brigade,
            @RequestParam(required = false) Integer agentId, 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long adresseId) {
        
        return statisticsService.getStatistiquesFiltrees(
            brigade,
            agentId,
            startDate,
            endDate,
            adresseId,
            isMission
        );
    }
}