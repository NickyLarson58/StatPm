package src.model;

import javax.persistence.*;

@Entity
@Table(name = "infractions")
public class Infraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_infraction;

    @Column(name = "natinf")
    private int natinf;

    @Column(name = "libelle")
    private String libelle;

    public Infraction() {
    }

    public Infraction(int natinf, String libelle) {
        this.natinf = natinf;
        this.libelle = libelle;
    }

    public long getId_infraction() {
        return id_infraction;
    }

    public int getNatinf() {
        return natinf;
    }

    public void setNatinf(int natinf) {
        this.natinf = natinf;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}