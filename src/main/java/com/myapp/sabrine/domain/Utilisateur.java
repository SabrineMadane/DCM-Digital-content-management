package com.myapp.sabrine.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "cv")
    private String cv;

    @NotNull
    @Column(name = "email_personnel", nullable = false)
    private String emailPersonnel;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_utilisateur__entreprises_desirees",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "entreprises_desirees_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateurs" }, allowSetters = true)
    private Set<Entreprise> entreprisesDesirees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCv() {
        return this.cv;
    }

    public Utilisateur cv(String cv) {
        this.setCv(cv);
        return this;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getEmailPersonnel() {
        return this.emailPersonnel;
    }

    public Utilisateur emailPersonnel(String emailPersonnel) {
        this.setEmailPersonnel(emailPersonnel);
        return this;
    }

    public void setEmailPersonnel(String emailPersonnel) {
        this.emailPersonnel = emailPersonnel;
    }

    public Set<Entreprise> getEntreprisesDesirees() {
        return this.entreprisesDesirees;
    }

    public void setEntreprisesDesirees(Set<Entreprise> entreprises) {
        this.entreprisesDesirees = entreprises;
    }

    public Utilisateur entreprisesDesirees(Set<Entreprise> entreprises) {
        this.setEntreprisesDesirees(entreprises);
        return this;
    }

    public Utilisateur addEntreprisesDesirees(Entreprise entreprise) {
        this.entreprisesDesirees.add(entreprise);
        return this;
    }

    public Utilisateur removeEntreprisesDesirees(Entreprise entreprise) {
        this.entreprisesDesirees.remove(entreprise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", cv='" + getCv() + "'" +
            ", emailPersonnel='" + getEmailPersonnel() + "'" +
            "}";
    }
}
