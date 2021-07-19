package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Etat;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * The Employee entity.
 */
@ApiModel(description = "The Employee entity.")
@Entity
@Table(name = "tache")
public class Tache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "intitule", nullable = false)
    private String intitule;

    @NotNull
    @Column(name = "date_limite", nullable = false)
    private Instant dateLimite;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private Etat etat;

    @Column(name = "date_debut")
    private Instant dateDebut;

    @Column(name = "date_fin")
    private Instant dateFin;

    @JsonIgnoreProperties(value = { "chef", "division", "cadres" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private IService service;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compte", "service", "tachesAffectes" }, allowSetters = true)
    private Employe cadreAffecte;

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

    public String getIntitule() {
        return this.intitule;
    }

    public Tache intitule(String intitule) {
        this.intitule = intitule;
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Instant getDateLimite() {
        return this.dateLimite;
    }

    public Tache dateLimite(Instant dateLimite) {
        this.dateLimite = dateLimite;
        return this;
    }

    public void setDateLimite(Instant dateLimite) {
        this.dateLimite = dateLimite;
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

    public Etat getEtat() {
        return this.etat;
    }

    public Tache etat(Etat etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Tache dateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Tache dateFin(Instant dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
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
            ", intitule='" + getIntitule() + "'" +
            ", dateLimite='" + getDateLimite() + "'" +
            ", description='" + getDescription() + "'" +
            ", etat='" + getEtat() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
