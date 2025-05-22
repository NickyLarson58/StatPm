package src.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import src.model.Commercant;
import java.util.List;

public interface CommercantRepository extends JpaRepository<Commercant, Long> {
    List<Commercant> findByNomCommerceContainingIgnoreCase(String nomCommerce);
    List<Commercant> findByAdresseCommerceContainingIgnoreCase(String adresseCommerce);
    List<Commercant> findByTelephones_NumeroContaining(String numero);
    List<Commercant> findByNomProprietaireContainingIgnoreCase(String nomProprietaire);
    List<Commercant> findByTelephoneProprietaireContaining(String telephoneProprietaire);
    List<Commercant> findByActivite_NomContainingIgnoreCase(String nomActivite);
}