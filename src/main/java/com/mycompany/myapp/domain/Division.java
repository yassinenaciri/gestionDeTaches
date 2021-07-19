package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Division.
 */
@Entity
@Table(name = "division")
public class Division implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_division", nullable = false)
    private String nomDivision;

    @JsonIgnoreProperties(value = { "compte", "service", "pole", "direction", "division" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Chef chef;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "chef", "direction", "divisions" }, allowSetters = true)
    private Pole pole;

    @OneToMany(mappedBy = "division")
    @JsonIgnoreProperties(value = { "chef", "division", "cadres" }, allowSetters = true)
    private Set<IService> services = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Division id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomDivision() {
        return this.nomDivision;
    }

    public Division nomDivision(String nomDivision) {
        this.nomDivision = nomDivision;
        return this;
    }

    public void setNomDivision(String nomDivision) {
        this.nomDivision = nomDivision;
    }

    public Chef getChef() {
        return this.chef;
    }

    public Division chef(Chef chef) {
        this.setChef(chef);
        return this;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public Pole getPole() {
        return this.pole;
    }

    public Division pole(Pole pole) {
        this.setPole(pole);
        return this;
    }

    public void setPole(Pole pole) {
        this.pole = pole;
    }

    public Set<IService> getServices() {
        return this.services;
    }

    public Division services(Set<IService> iServices) {
        this.setServices(iServices);
        return this;
    }

    public Division addServices(IService iService) {
        this.services.add(iService);
        iService.setDivision(this);
        return this;
    }

    public Division removeServices(IService iService) {
        this.services.remove(iService);
        iService.setDivision(null);
        return this;
    }

    public void setServices(Set<IService> iServices) {
        if (this.services != null) {
            this.services.forEach(i -> i.setDivision(null));
        }
        if (iServices != null) {
            iServices.forEach(i -> i.setDivision(this));
        }
        this.services = iServices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Division)) {
            return false;
        }
        return id != null && id.equals(((Division) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Division{" +
            "id=" + getId() +
            ", nomDivision='" + getNomDivision() + "'" +
            "}";
    }
}
