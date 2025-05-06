package src.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import src.model.Agents;

@Repository
public interface AgentsRepository extends JpaRepository<Agents, Long> {
    Optional<Agents> findByMatricule(int matricule);

    Boolean existsByMatricule(int matricule);

    Optional<Agents> findAgentsByMatriculeAndMdp(int matricule, int mdp);
}