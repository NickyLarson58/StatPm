package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.model.Telephone;

public interface TelephoneRepository extends JpaRepository<Telephone, Long> {
}