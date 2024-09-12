package com.example.service.impl;

import com.example.config.ApiResponse;
import com.example.domain.Role;
import com.example.domain.Utilisateur;
import com.example.dto.UtilisateurDTO;
import com.example.dto.mapper.UtilisateurMapper;
import com.example.repository.UtilisateurRepo;
import com.example.repository.RoleRepo;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurServiceImpl.class);

    @Autowired
    private UtilisateurRepo utilisateurRepository;

    @Autowired
    private RoleRepo RoleRepo;

    @Autowired
    private UsersResource usersResource;

    public ApiResponse addUser(UtilisateurDTO utilisateurDTO) throws IOException {
        // Vérification des champs obligatoires
        if (utilisateurDTO.getEmail() == null || utilisateurDTO.getPassword() == null) {
            return new ApiResponse(400, "Email et mot de passe sont requis");
        }

        try {
            // Création de l'utilisateur dans Keycloak
            UserRepresentation user = new UserRepresentation();
            user.setUsername(utilisateurDTO.getEmail());
            user.setEmail(utilisateurDTO.getEmail());
            user.setFirstName(utilisateurDTO.getNom());
            user.setLastName(utilisateurDTO.getPrenom());
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(utilisateurDTO.getPassword());
            user.setCredentials(Collections.singletonList(credential));

            Response response = usersResource.create(user);
            int status = response.getStatus();
            log.info("User creation response status: {}", status);

            if (status != 201) {
                throw new IOException("Erreur lors de la création de l'utilisateur dans Keycloak");
            }

            // Récupération de l'ID utilisateur de Keycloak
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            log.info("Extracted Keycloak user ID: {}", userId);



            // Création de l'utilisateur dans la base de données locale
            Utilisateur utilisateur = UtilisateurMapper.toEntity(utilisateurDTO);
            utilisateur.setKeycloakId(userId);

            // Assurez-vous d'initialiser correctement le rôle
            Role role = RoleRepo.findById(utilisateurDTO.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            utilisateur.setRole(role);
            log.info("User's Role  : {}", utilisateur.getRole());
            utilisateurRepository.save(utilisateur);
            log.info("User successfully saved in the local database with ID: {}", utilisateur.getId());

            return new ApiResponse(201, "Utilisateur créé avec succès");
        } catch (Exception e) {
            log.error("Exception pendant la création de l'utilisateur: ", e);
            throw new IOException("Erreur lors de la création de l'utilisateur", e);
        }
    }


    public UtilisateurDTO getUserById(Long id) throws IOException {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur != null) {
            return UtilisateurMapper.toDTO(utilisateur);
        }
        return null;
    }

    public List<UtilisateurDTO> getAllUsers() throws IOException {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        return utilisateurs.stream().map(UtilisateurMapper::toDTO).collect(Collectors.toList());
    }

    public ApiResponse updateUser(UtilisateurDTO utilisateurDTO) throws IOException {
        try {
            // Récupération de l'utilisateur depuis la base de données locale
            Utilisateur utilisateur = utilisateurRepository.findById(utilisateurDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            // Mise à jour dans Keycloak
            UserRepresentation userRepresentation = usersResource.get(utilisateur.getKeycloakId()).toRepresentation();
            userRepresentation.setFirstName(utilisateurDTO.getNom());
            userRepresentation.setLastName(utilisateurDTO.getPrenom());
            userRepresentation.setEmail(utilisateurDTO.getEmail());

            if (utilisateurDTO.getPassword() != null && !utilisateurDTO.getPassword().isEmpty()) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setTemporary(false);
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(utilisateurDTO.getPassword());
                userRepresentation.setCredentials(Collections.singletonList(credential));
            }

            usersResource.get(utilisateur.getKeycloakId()).update(userRepresentation);

            // Mise à jour dans la base de données locale
            UtilisateurMapper.updateEntity(utilisateurDTO, utilisateur);

            // Mise à jour du rôle si nécessaire
            if (utilisateurDTO.getRoleId() != null && !utilisateurDTO.getRoleId().equals(utilisateur.getRole().getId())) {
                Role newRole = RoleRepo.findById(utilisateurDTO.getRoleId())
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                utilisateur.setRole(newRole);
            }

            utilisateurRepository.save(utilisateur);

            log.info("Utilisateur mis à jour avec succès: {}", utilisateur.getEmail());
            return new ApiResponse(200, "Utilisateur mis à jour avec succès");
        } catch (Exception e) {
            log.error("Exception pendant la mise à jour de l'utilisateur: ", e);
            throw new IOException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }


    public ApiResponse deleteUser(Long id) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
            if (utilisateur != null) {
                // Suppression dans Keycloak
                usersResource.get(utilisateur.getKeycloakId()).remove();
                // Suppression dans la base de données locale
                utilisateurRepository.deleteById(id);
                log.info("Utilisateur supprimé avec succès: {}", id);
                return new ApiResponse(200, "Utilisateur supprimé");
            } else {
                return new ApiResponse(404, "Utilisateur non trouvé");
            }
        } catch (Exception e) {
            log.error("Exception pendant la suppression de l'utilisateur: ", e);
            return new ApiResponse(500, "Erreur lors de la suppression de l'utilisateur");
        }
    }

    public ApiResponse deleteUserByEmail(String email) throws IOException {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
            if (utilisateur != null) {
                return deleteUser(utilisateur.getId());
            } else {
                return new ApiResponse(404, "Utilisateur non trouvé");
            }
        } catch (Exception e) {
            log.error("Exception pendant la suppression de l'utilisateur par email: ", e);
            throw new IOException("Erreur lors de la suppression de l'utilisateur par email", e);
        }
    }


    public boolean userExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
}
