package src.model;

import javax.persistence.*;

@Entity
@Table(name = "adresses")
public class Adresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idadresse;
  
    @Column(name = "nomadresse")
    private String nomadresse;

    public Adresses() {
    }

    public Adresses(String nomadresse) {
        this.nomadresse = nomadresse;
    }

    public long getIdadresse() {
        return idadresse;
    }

    public void setIdadresse(long idadresse) {
        this.idadresse = idadresse;
    }

    public String getNomadresse() {
        return nomadresse;
    }

    public void setNomadresse(String nomadresse) {
        this.nomadresse = nomadresse;
    }

    @Override
    public String toString() {
        return "Adresses [idadresse=" + idadresse + ", nomadresse=" + nomadresse + "]";
    }
}
