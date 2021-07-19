package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Direction.
 */
@Entity
@Table(name = "direction")
public class Direction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_direction", nullable = false)
    private String nomDirection;

    @JsonIgnoreProperties(value = { "compte", "service", "pole", "direction", "division" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Chef directeur;

    @OneToMany(mappedBy = "direction")
    @JsonIgnoreProperties(value = { "chef", "direction", "divisions" }, allowSetters = true)
    private Set<Pole> poles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Direction id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomDirection() {
        return this.nomDirection;
    }

    public Direction nomDirection(String nomDirection) {
        this.nomDirection = nomDirection;
        return this;
    }

    public void setNomDirection(String nomDirection) {
        this.nomDirection = nomDirection;
    }

    public Chef getDirecteur() {
        return this.directeur;
    }

    public Direction directeur(Chef chef) {
        this.setDirecteur(chef);
        return this;
    }

    public void setDirecteur(Chef chef) {
        this.directeur = chef;
    }

    public Set<Pole> getPoles() {
        return this.poles;
    }

    public Direction poles(Set<Pole> poles) {
        this.setPoles(poles);
        return this;
    }

    public Direction addPoles(Pole pole) {
        this.poles.add(pole);
        pole.setDirection(this);
        return this;
    }

    public Direction removePoles(Pole pole) {
        this.poles.remove(pole);
        pole.setDirection(null);
        return this;
    }

    public void setPoles(Set<Pole> poles) {
        if (this.poles != null) {
            this.poles.forEach(i -> i.setDirection(null));
        }
        if (poles != null) {
            poles.forEach(i -> i.setDirection(this));
        }
        this.poles = poles;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Direction)) {
            return false;
        }
        return id != null && id.equals(((Direction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Direction{" +
            "id=" + getId() +
            ", nomDirection='" + getNomDirection() + "'" +
            "}";
    }
}
