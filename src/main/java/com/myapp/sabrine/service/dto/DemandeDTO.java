package com.myapp.sabrine.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.sabrine.domain.Demande} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemandeDTO implements Serializable {

    private Long id;

    @NotNull
    private String etat;

    @NotNull
    private ZonedDateTime dateCandidature;

    private UtilisateurDTO utilisateur;

    private EntrepriseDTO entreprise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public ZonedDateTime getDateCandidature() {
        return dateCandidature;
    }

    public void setDateCandidature(ZonedDateTime dateCandidature) {
        this.dateCandidature = dateCandidature;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public EntrepriseDTO getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(EntrepriseDTO entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeDTO)) {
            return false;
        }

        DemandeDTO demandeDTO = (DemandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeDTO{" +
            "id=" + getId() +
            ", etat='" + getEtat() + "'" +
            ", dateCandidature='" + getDateCandidature() + "'" +
            ", utilisateur=" + getUtilisateur() +
            ", entreprise=" + getEntreprise() +
            "}";
    }
}
