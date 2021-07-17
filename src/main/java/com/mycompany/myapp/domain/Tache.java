package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Tache.
 */
@Entity
@Table(name = "tache")
public class Tache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "dure_estime", nullable = false)
    private Duration dureEstime;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "etat")
    private Boolean etat;

    @JsonIgnoreProperties(value = { "chef", "division", "cadres" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private IService service;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compte", "service", "tachesAffectes", "affectations" }, allowSetters = true)
    private Employe cadreAffecte;

    @OneToMany(mappedBy = "tache")
    @JsonIgnoreProperties(value = { "tache", "cadre" }, allowSetters = true)
    private Set<Historique> affectations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tache id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Tache nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Duration getDureEstime() {
        return this.dureEstime;
    }

    public Tache dureEstime(Duration dureEstime) {
        this.dureEstime = dureEstime;
        return this;
    }

    public void setDureEstime(Duration dureEstime) {
        this.dureEstime = dureEstime;
    }

    public String getDescription() {
        return this.description;
    }

    public Tache description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEtat() {
        return this.etat;
    }

    public Tache etat(Boolean etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public IService getService() {
        return this.service;
    }

    public Tache service(IService iService) {
        this.setService(iService);
        return this;
    }

    public void setService(IService iService) {
        this.service = iService;
    }

    public Employe getCadreAffecte() {
        return this.cadreAffecte;
    }

    public Tache cadreAffecte(Employe employe) {
        this.setCadreAffecte(employe);
        return this;
    }

    public void setCadreAffecte(Employe employe) {
        this.cadreAffecte = employe;
    }

    public Set<Historique> getAffectations() {
        return this.affectations;
    }

    public Tache affectations(Set<Historique> historiques) {
        this.setAffectations(historiques);
        return this;
    }

    public Tache addAffectations(Historique historique) {
        this.affectations.add(historique);
        historique.setTache(this);
        return this;
    }

    public Tache removeAffectations(Historique historique) {
        this.affectations.remove(historique);
        historique.setTache(null);
        return this;
    }

    public void setAffectations(Set<Historique> historiques) {
        if (this.affectations != null) {
            this.affectations.forEach(i -> i.setTache(null));
        }
        if (historiques != null) {
            historiques.forEach(i -> i.setTache(this));
        }
        this.affectations = historiques;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tache)) {
            return false;
        }
        return id != null && id.equals(((Tache) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tache{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", dureEstime='" + getDureEstime() + "'" +
            ", description='" + getDescription() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
