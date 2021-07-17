package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A IService.
 */
@Entity
@Table(name = "i_service")
public class IService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @JsonIgnoreProperties(value = { "compte", "service", "pole", "direction", "division" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Chef chef;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "chef", "pole", "services" }, allowSetters = true)
    private Division division;

    @OneToMany(mappedBy = "service")
    @JsonIgnoreProperties(value = { "compte", "service", "tachesAffectes", "affectations" }, allowSetters = true)
    private Set<Employe> cadres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IService id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public IService nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Chef getChef() {
        return this.chef;
    }

    public IService chef(Chef chef) {
        this.setChef(chef);
        return this;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public Division getDivision() {
        return this.division;
    }

    public IService division(Division division) {
        this.setDivision(division);
        return this;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Set<Employe> getCadres() {
        return this.cadres;
    }

    public IService cadres(Set<Employe> employes) {
        this.setCadres(employes);
        return this;
    }

    public IService addCadres(Employe employe) {
        this.cadres.add(employe);
        employe.setService(this);
        return this;
    }

    public IService removeCadres(Employe employe) {
        this.cadres.remove(employe);
        employe.setService(null);
        return this;
    }

    public void setCadres(Set<Employe> employes) {
        if (this.cadres != null) {
            this.cadres.forEach(i -> i.setService(null));
        }
        if (employes != null) {
            employes.forEach(i -> i.setService(this));
        }
        this.cadres = employes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IService)) {
            return false;
        }
        return id != null && id.equals(((IService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IService{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
