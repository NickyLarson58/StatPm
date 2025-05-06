package src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import src.model.Mad;

public interface MadRepository extends JpaRepository<Mad, Long> {

    @Query(value = "SELECT libelle FROM mad", nativeQuery = true)
    List<String> findAllNomMad();
}