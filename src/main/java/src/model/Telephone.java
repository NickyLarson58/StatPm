package src.model;

import javax.persistence.*;

@Entity
public class Telephone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String numero;
    private String statut;

    @ManyToOne
    @JoinColumn(name = "commerce_id")
    private Commercant commerce;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Commercant getCommerce() { return commerce; }
    public void setCommerce(Commercant commerce) { this.commerce = commerce; }
}