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
 * A Entreprise.
 */
@Entity
@Table(name = "entreprise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Entreprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "secteur", nullable = false)
    private String secteur;

    @NotNull
    @Column(name = "presence_au_maroc", nullable = false)
    private Boolean presenceAuMaroc;

    @Column(name = "focus")
    private String focus;

    @Column(name = "logo")
    private String logo;

    @Column(name = "profil_linked_in")
    private String profilLinkedIn;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "entreprisesDesirees")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entreprisesDesirees" }, allowSetters = true)
    private Set<Utilisateur> utilisateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Entreprise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecteur() {
        return this.secteur;
    }

    public Entreprise secteur(String secteur) {
        this.setSecteur(secteur);
        return this;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public Boolean getPresenceAuMaroc() {
        return this.presenceAuMaroc;
    }

    public Entreprise presenceAuMaroc(Boolean presenceAuMaroc) {
        this.setPresenceAuMaroc(presenceAuMaroc);
        return this;
    }

    public void setPresenceAuMaroc(Boolean presenceAuMaroc) {
        this.presenceAuMaroc = presenceAuMaroc;
    }

    public String getFocus() {
        return this.focus;
    }

    public Entreprise focus(String focus) {
        this.setFocus(focus);
        return this;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getLogo() {
        return this.logo;
    }

    public Entreprise logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProfilLinkedIn() {
        return this.profilLinkedIn;
    }

    public Entreprise profilLinkedIn(String profilLinkedIn) {
        this.setProfilLinkedIn(profilLinkedIn);
        return this;
    }

    public void setProfilLinkedIn(String profilLinkedIn) {
        this.profilLinkedIn = profilLinkedIn;
    }

    public Set<Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        if (this.utilisateurs != null) {
            this.utilisateurs.forEach(i -> i.removeEntreprisesDesirees(this));
        }
        if (utilisateurs != null) {
            utilisateurs.forEach(i -> i.addEntreprisesDesirees(this));
        }
        this.utilisateurs = utilisateurs;
    }

    public Entreprise utilisateurs(Set<Utilisateur> utilisateurs) {
        this.setUtilisateurs(utilisateurs);
        return this;
    }

    public Entreprise addUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.add(utilisateur);
        utilisateur.getEntreprisesDesirees().add(this);
        return this;
    }

    public Entreprise removeUtilisateur(Utilisateur utilisateur) {
        this.utilisateurs.remove(utilisateur);
        utilisateur.getEntreprisesDesirees().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entreprise)) {
            return false;
        }
        return getId() != null && getId().equals(((Entreprise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Entreprise{" +
            "id=" + getId() +
            ", secteur='" + getSecteur() + "'" +
            ", presenceAuMaroc='" + getPresenceAuMaroc() + "'" +
            ", focus='" + getFocus() + "'" +
            ", logo='" + getLogo() + "'" +
            ", profilLinkedIn='" + getProfilLinkedIn() + "'" +
            "}";
    }
}
