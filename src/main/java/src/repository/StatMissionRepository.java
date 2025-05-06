package src.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import src.model.StatMission;

@Repository
public interface StatMissionRepository extends JpaRepository<StatMission, Long> {

    List<StatMission> findAllByBrigadeAndLieuMission_IdadresseAndDateMissionBetween(String brigade, Long adresseId, LocalDate startDate, LocalDate endDate);
    List<StatMission> findAllByBrigadeAndDateMissionBetween(String brigade, LocalDate startDate, LocalDate endDate);

    List<StatMission> findAllByAgents_MatriculeAndLieuMission_IdadresseAndDateMissionBetween(int matricule, Long adresseId, LocalDate startDate, LocalDate endDate);
    List<StatMission> findAllByAgents_MatriculeAndDateMissionBetween(int matricule, LocalDate startDate, LocalDate endDate);

    List<StatMission> findAllByDateMissionBetweenAndLieuMission_Idadresse(LocalDate startDate, LocalDate endDate, Long adresseId);
    List<StatMission> findAllByDateMissionBetween(LocalDate startDate, LocalDate endDate);
}