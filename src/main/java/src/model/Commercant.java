package src.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Commercant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomCommerce;
    private String typeActivite;
    private String adresseCommerce;
    private String joursHeuresOuverture;
    private String systemeProtection;
    private String nomProprietaire;
    private String prenomProprietaire;
    private String adresseProprietaire;
    private String telephoneProprietaire;
    private String mailProprietaire;
    @Column(name = "numAdresseCommerce")
    private String numeroAdresse;

    @OneToMany(mappedBy = "commerce", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Telephone> telephones;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomCommerce() { return nomCommerce; }
    public void setNomCommerce(String nomCommerce) { this.nomCommerce = nomCommerce; }
    public String getTypeActivite() { return typeActivite; }
    public void setTypeActivite(String typeActivite) { this.typeActivite = typeActivite; }
    public String getAdresseCommerce() { return adresseCommerce; }
    public void setAdresseCommerce(String adresseCommerce) { this.adresseCommerce = adresseCommerce; }
    public String getJoursHeuresOuverture() { return joursHeuresOuverture; }
    public void setJoursHeuresOuverture(String joursHeuresOuverture) { this.joursHeuresOuverture = joursHeuresOuverture; }
    public String getSystemeProtection() { return systemeProtection; }
    public void setSystemeProtection(String systemeProtection) { this.systemeProtection = systemeProtection; }
    public String getNomProprietaire() { return nomProprietaire; }
    public void setNomProprietaire(String nomProprietaire) { this.nomProprietaire = nomProprietaire; }
    public String getPrenomProprietaire() { return prenomProprietaire; }
    public void setPrenomProprietaire(String prenomProprietaire) { this.prenomProprietaire = prenomProprietaire; }
    public String getAdresseProprietaire() { return adresseProprietaire; }
    public void setAdresseProprietaire(String adresseProprietaire) { this.adresseProprietaire = adresseProprietaire; }
    public String getTelephoneProprietaire() { return telephoneProprietaire; }
    public void setTelephoneProprietaire(String telephoneProprietaire) { this.telephoneProprietaire = telephoneProprietaire; }
    public String getMailProprietaire() { return mailProprietaire; }
    public void setMailProprietaire(String mailProprietaire) { this.mailProprietaire = mailProprietaire; }
    public List<Telephone> getTelephones() { return telephones; }
    public void setTelephones(List<Telephone> telephones) { this.telephones = telephones; }
    public String getNumeroAdresse() { return numeroAdresse; }
    public void setNumeroAdresse(String numeroAdresse) { this.numeroAdresse = numeroAdresse; }
}