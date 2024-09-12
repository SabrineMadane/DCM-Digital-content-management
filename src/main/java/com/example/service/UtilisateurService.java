package com.example.service;

import com.example.dto.UtilisateurDTO;

import java.util.List;

public interface UtilisateurService {
    UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO);

    List<UtilisateurDTO> getAllUtilisateurs();

    UtilisateurDTO getUtilisateurById(Long id);

    UtilisateurDTO updateUtilisateur(Long id, UtilisateurDTO updatedUtilisateurDTO);

    void deleteUtilisateur(Long id);
}
