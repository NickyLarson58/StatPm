package src.model;

import javax.persistence.*;

@Entity
@Table(name = "agents")
public class Agents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matricule;

    @Column(name = "nom_Agent", columnDefinition = "VARCHAR(255)")
    private String nomAgent;
    
    @Column(name = "prenom_Agent", columnDefinition = "VARCHAR(255)")
    private String prenomAgent;
    
    @Column(name = "brigade")
    private String brigade;
    
    @Column(name = "mdp")
    private int mdp;
    
    @Column(name = "pouvoir")
    private String pouvoir;
    
    @Column(name = "secteur")
    private String secteur;
    
    public Agents() {
    }
    
    public Agents(String nomAgent, String prenomAgent, String brigade, int mdp, String pouvoir, String secteur) {
        this.nomAgent = nomAgent;
        this.prenomAgent = prenomAgent;
        this.brigade = brigade;
        this.mdp = mdp;
        this.pouvoir = pouvoir;
        this.secteur = secteur;
    }
    
    public int getMatricule() {
        return matricule;
    }

    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }
    
    public String getNomAgent() {
        return nomAgent;
    }
    
    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }
    
    public String getPrenomAgent() {
        return prenomAgent;
    }
    
    public void setPrenomAgent(String prenomAgent) {
        this.prenomAgent = prenomAgent;
    }
    
    public String getBrigade() {
        return brigade;
    }
    
    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }
    
    public int getMdp() {
        return mdp;
    }
    
    public void setMdp(int mdp) {
        this.mdp = mdp;
    }
    
    public String getPouvoir() {
        return pouvoir;
    }
    
    public void setPouvoir(String pouvoir) {
        this.pouvoir = pouvoir;
    }
    
    public String getSecteur() {
        return secteur;
    }
    
    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }
}