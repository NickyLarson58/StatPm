package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import src.model.TypeSignalement;

@Repository
public interface TypeSignalementRepository extends JpaRepository<TypeSignalement, Long> {
}