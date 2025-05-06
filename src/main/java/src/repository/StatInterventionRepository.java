package src.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import src.model.StatIntervention;

@Repository
public interface StatInterventionRepository extends JpaRepository<StatIntervention, Long> {

    List<StatIntervention> findAllByBrigadeAndAdresse_IdadresseAndDateInterventionsBetween(String brigade, Long adresseId, LocalDate startDate, LocalDate endDate);
    List<StatIntervention> findAllByBrigadeAndDateInterventionsBetween(String brigade, LocalDate startDate, LocalDate endDate);

    List<StatIntervention> findAllByAgents_MatriculeAndAdresse_IdadresseAndDateInterventionsBetween(int matricule, Long adresseId, LocalDate startDate, LocalDate endDate);
    List<StatIntervention> findAllByAgents_MatriculeAndDateInterventionsBetween(int matricule, LocalDate startDate, LocalDate endDate);

    List<StatIntervention> findAllByAdresse_IdadresseAndDateInterventionsBetween(Long adresseId, LocalDate startDate, LocalDate endDate);
    List<StatIntervention> findAllByDateInterventionsBetween(LocalDate startDate, LocalDate endDate);
}