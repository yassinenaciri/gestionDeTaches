package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Chef.
 */
@Entity
@Table(name = "chef")
public class Chef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;

    @Column(name = "role")
    private String role;

    @OneToOne
    @JoinColumn(unique = true)
    private User compte;

    @JsonIgnoreProperties(value = { "chef", "division", "cadres" }, allowSetters = true)
    @OneToOne(mappedBy = "chef")
    private IService service;

    @JsonIgnoreProperties(value = { "chef", "direction", "divisions" }, allowSetters = true)
    @OneToOne(mappedBy = "chef")
    private Pole pole;

    @JsonIgnoreProperties(value = { "directeur", "poles" }, allowSetters = true)
    @OneToOne(mappedBy = "directeur")
    private Direction direction;

    @JsonIgnoreProperties(value = { "chef", "pole", "services" }, allowSetters = true)
    @OneToOne(mappedBy = "chef")
    private Division division;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chef id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomComplet() {
        return this.nomComplet;
    }

    public Chef nomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
        return this;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getRole() {
        return this.role;
    }

    public Chef role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getCompte() {
        return this.compte;
    }

    public Chef compte(User user) {
        this.setCompte(user);
        return this;
    }

    public void setCompte(User user) {
        this.compte = user;
    }

    public IService getService() {
        return this.service;
    }

    public Chef service(IService iService) {
        this.setService(iService);
        return this;
    }

    public void setService(IService iService) {
        if (this.service != null) {
            this.service.setChef(null);
        }
        if (iService != null) {
            iService.setChef(this);
        }
        this.service = iService;
    }

    public Pole getPole() {
        return this.pole;
    }

    public Chef pole(Pole pole) {
        this.setPole(pole);
        return this;
    }

    public void setPole(Pole pole) {
        if (this.pole != null) {
            this.pole.setChef(null);
        }
        if (pole != null) {
            pole.setChef(this);
        }
        this.pole = pole;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Chef direction(Direction direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(Direction direction) {
        if (this.direction != null) {
            this.direction.setDirecteur(null);
        }
        if (direction != null) {
            direction.setDirecteur(this);
        }
        this.direction = direction;
    }

    public Division getDivision() {
        return this.division;
    }

    public Chef division(Division division) {
        this.setDivision(division);
        return this;
    }

    public void setDivision(Division division) {
        if (this.division != null) {
            this.division.setChef(null);
        }
        if (division != null) {
            division.setChef(this);
        }
        this.division = division;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chef)) {
            return false;
        }
        return id != null && id.equals(((Chef) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chef{" +
            "id=" + getId() +
            ", nomComplet='" + getNomComplet() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
