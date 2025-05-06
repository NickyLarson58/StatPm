package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import src.model.Brigade;
import java.util.Optional;

@Repository
public interface BrigadeRepository extends JpaRepository<Brigade, Long> {
    Optional<Brigade> findByNomBrigade(String nomBrigade);
}