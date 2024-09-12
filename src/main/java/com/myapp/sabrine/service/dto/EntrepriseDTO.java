package com.myapp.sabrine.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.myapp.sabrine.domain.Entreprise} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntrepriseDTO implements Serializable {

    private Long id;

    @NotNull
    private String secteur;

    @NotNull
    private Boolean presenceAuMaroc;

    private String focus;

    private String logo;

    private String profilLinkedIn;

    private Set<UtilisateurDTO> utilisateurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public Boolean getPresenceAuMaroc() {
        return presenceAuMaroc;
    }

    public void setPresenceAuMaroc(Boolean presenceAuMaroc) {
        this.presenceAuMaroc = presenceAuMaroc;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProfilLinkedIn() {
        return profilLinkedIn;
    }

    public void setProfilLinkedIn(String profilLinkedIn) {
        this.profilLinkedIn = profilLinkedIn;
    }

    public Set<UtilisateurDTO> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Set<UtilisateurDTO> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntrepriseDTO)) {
            return false;
        }

        EntrepriseDTO entrepriseDTO = (EntrepriseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, entrepriseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntrepriseDTO{" +
            "id=" + getId() +
            ", secteur='" + getSecteur() + "'" +
            ", presenceAuMaroc='" + getPresenceAuMaroc() + "'" +
            ", focus='" + getFocus() + "'" +
            ", logo='" + getLogo() + "'" +
            ", profilLinkedIn='" + getProfilLinkedIn() + "'" +
            ", utilisateurs=" + getUtilisateurs() +
            "}";
    }
}
