package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import src.model.Infraction;
import java.util.List;

public interface InfractionRepository extends JpaRepository<Infraction, Long> {
    
    @Query(value = "SELECT natinf, libelle FROM infractions", nativeQuery = true)
    List<String> findAllNomInfraction();
}