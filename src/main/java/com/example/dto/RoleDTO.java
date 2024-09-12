package com.example.dto;

import java.util.Map;
import java.util.Set;

public class RoleDTO {
    private Long id;
    private String nom;
    private String description;
    private Boolean etat;
    private Map<String, Boolean> libraryPermissions; // Map des permissions avec noms comme clés
    private Map<String, Boolean> gestionPermissions; // Map des permissions avec noms comme clés
    private Set<Long> userIds; // Liste des IDs des utilisateurs associés

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public Map<String, Boolean> getLibraryPermissions() {
        return libraryPermissions;
    }

    public void setLibraryPermissions(Map<String, Boolean> libraryPermissions) {
        this.libraryPermissions = libraryPermissions;
    }

    public Map<String, Boolean> getGestionPermissions() {
        return gestionPermissions;
    }

    public void setGestionPermissions(Map<String, Boolean> gestionPermissions) {
        this.gestionPermissions = gestionPermissions;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }
}
