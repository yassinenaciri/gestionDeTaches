package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Pole.
 */
@Entity
@Table(name = "pole")
public class Pole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_pole", nullable = false)
    private String nomPole;

    @JsonIgnoreProperties(value = { "compte", "service", "pole", "direction", "division" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Chef chef;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "directeur", "poles" }, allowSetters = true)
    private Direction direction;

    @OneToMany(mappedBy = "pole")
    @JsonIgnoreProperties(value = { "chef", "pole", "services" }, allowSetters = true)
    private Set<Division> divisions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pole id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomPole() {
        return this.nomPole;
    }

    public Pole nomPole(String nomPole) {
        this.nomPole = nomPole;
        return this;
    }

    public void setNomPole(String nomPole) {
        this.nomPole = nomPole;
    }

    public Chef getChef() {
        return this.chef;
    }

    public Pole chef(Chef chef) {
        this.setChef(chef);
        return this;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Pole direction(Direction direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Set<Division> getDivisions() {
        return this.divisions;
    }

    public Pole divisions(Set<Division> divisions) {
        this.setDivisions(divisions);
        return this;
    }

    public Pole addDivisions(Division division) {
        this.divisions.add(division);
        division.setPole(this);
        return this;
    }

    public Pole removeDivisions(Division division) {
        this.divisions.remove(division);
        division.setPole(null);
        return this;
    }

    public void setDivisions(Set<Division> divisions) {
        if (this.divisions != null) {
            this.divisions.forEach(i -> i.setPole(null));
        }
        if (divisions != null) {
            divisions.forEach(i -> i.setPole(this));
        }
        this.divisions = divisions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pole)) {
            return false;
        }
        return id != null && id.equals(((Pole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pole{" +
            "id=" + getId() +
            ", nomPole='" + getNomPole() + "'" +
            "}";
    }
}
