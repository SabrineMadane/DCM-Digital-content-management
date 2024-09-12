package com.example.dto.mapper;


import com.example.domain.Utilisateur;
import com.example.dto.UtilisateurDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UtilisateurMapper {

    public static UtilisateurDTO toDTO(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setFonction(utilisateur.getFonction());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setStatut(utilisateur.getStatut());

        // Convertit le rôle en ID
        utilisateurDTO.setRoleId(utilisateur.getRole() != null ? utilisateur.getRole().getId() : null);

        // Convertit les fichiers en IDs
        List<Long> fileIds = utilisateur.getFiles().stream()
                .map(file -> file.getId())
                .collect(Collectors.toList());
        utilisateurDTO.setFileIds(fileIds);

        return utilisateurDTO;
    }

    public static Utilisateur toEntity(UtilisateurDTO utilisateurDTO) {
        if (utilisateurDTO == null) {
            return null;
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(utilisateurDTO.getId());
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setFonction(utilisateurDTO.getFonction());
        utilisateur.setEmail(utilisateurDTO.getEmail());
        utilisateur.setStatut(utilisateurDTO.getStatut());

        // Note: Vous devrez peut-être récupérer le rôle et les fichiers à partir de leurs IDs
        // Exemple : utilisateur.setRole(roleRepository.findById(utilisateurDTO.getRoleId()).orElse(null));
        // Exemple : utilisateur.setFiles(fileRepository.findAllById(utilisateurDTO.getFileIds()));

        return utilisateur;
    }

    public static void updateEntity(UtilisateurDTO utilisateurDTO, Utilisateur utilisateur) {
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setFonction(utilisateurDTO.getFonction());
        utilisateur.setEmail(utilisateurDTO.getEmail());
        utilisateur.setStatut(utilisateurDTO.getStatut());

        // Note: Vous devrez peut-être mettre à jour le rôle et les fichiers à partir de leurs IDs
        // Exemple : utilisateur.setRole(roleRepository.findById(utilisateurDTO.getRoleId()).orElse(null));
        // Exemple : utilisateur.setFiles(fileRepository.findAllById(utilisateurDTO.getFileIds()));
    }
}

