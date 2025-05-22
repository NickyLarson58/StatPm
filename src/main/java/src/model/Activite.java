package src.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activites")
public class Activite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    public Activite() {}

    public Activite(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activite activite = (Activite) o;
        return Objects.equals(id, activite.id) && Objects.equals(nom, activite.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom);
    }
}