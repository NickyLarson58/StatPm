package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}