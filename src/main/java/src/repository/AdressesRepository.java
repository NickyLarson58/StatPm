package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import src.model.Adresses;


@Repository
public interface AdressesRepository extends JpaRepository<Adresses, Long> {
}