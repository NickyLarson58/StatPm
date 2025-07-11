package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import src.model.Interventions;


@Repository
public interface InterventionsRepository extends JpaRepository<Interventions, Integer> {
}