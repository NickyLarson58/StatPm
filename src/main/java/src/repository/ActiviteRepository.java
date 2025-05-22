package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.Activite;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {
    Activite findByNom(String nom);
}