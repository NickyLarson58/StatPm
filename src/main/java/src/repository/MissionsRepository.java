package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import src.model.Missions;

import java.util.List;

public interface MissionsRepository extends JpaRepository<Missions, Long> {
    List<Missions> findByNomMission(String nomMission);
}