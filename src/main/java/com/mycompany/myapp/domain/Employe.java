package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Employe.
 */
@Entity
@Table(name = "employe")
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;

    @OneToOne
    @JoinColumn(unique = true)
    private User compte;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "chef", "division", "cadres" }, allowSetters = true)
    private IService service;

    @OneToMany(mappedBy = "cadreAffecte")
    @JsonIgnoreProperties(value = { "service", "cadreAffecte" }, allowSetters = true)
    private Set<Tache> tachesAffectes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employe id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomComplet() {
        return this.nomComplet;
    }

    public Employe nomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
        return this;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public User getCompte() {
        return this.compte;
    }

    public Employe compte(User user) {
        this.setCompte(user);
        return this;
    }

    public void setCompte(User user) {
        this.compte = user;
    }

    public IService getService() {
        return this.service;
    }

    public Employe service(IService iService) {
        this.setService(iService);
        return this;
    }

    public void setService(IService iService) {
        this.service = iService;
    }

    public Set<Tache> getTachesAffectes() {
        return this.tachesAffectes;
    }

    public Employe tachesAffectes(Set<Tache> taches) {
        this.setTachesAffectes(taches);
        return this;
    }

    public Employe addTachesAffecte(Tache tache) {
        this.tachesAffectes.add(tache);
        tache.setCadreAffecte(this);
        return this;
    }

    public Employe removeTachesAffecte(Tache tache) {
        this.tachesAffectes.remove(tache);
        tache.setCadreAffecte(null);
        return this;
    }

    public void setTachesAffectes(Set<Tache> taches) {
        if (this.tachesAffectes != null) {
            this.tachesAffectes.forEach(i -> i.setCadreAffecte(null));
        }
        if (taches != null) {
            taches.forEach(i -> i.setCadreAffecte(this));
        }
        this.tachesAffectes = taches;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employe)) {
            return false;
        }
        return id != null && id.equals(((Employe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employe{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            "}";
    }
}
