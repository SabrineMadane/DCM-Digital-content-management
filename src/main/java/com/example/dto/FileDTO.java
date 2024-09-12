package com.example.dto;

import java.util.Set;

public class FileDTO {
    private Long id; // Utiliser un String pour stocker l'UUID

    private String uuid; // Champ UUID pour la correspondance avec MinIO

    private String fileName;
    private String description;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String type;
    private String chemin;
    private String statut; // Utilisation d'un String pour le statut au lieu de l'énumération
    private Long userId;
    private Set<Long> tagIds; // Liste des IDs des tags associés

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    // Getters et Setters
    private String Url;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
