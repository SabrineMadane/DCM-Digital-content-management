package com.example.config;

import com.example.dto.UserDTO;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakServiceImpl  {


    private static final Logger log = LoggerFactory.getLogger(KeycloakServiceImpl.class);

    @Autowired
    private UsersResource usersResource;

    @Autowired
    private Keycloak keycloak;


    public ApiResponse addUser(UserDTO userDTO) throws IOException {
        try {
            // Création de User
            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getEmail());
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEnabled(true);

            // Création de mdp
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(userDTO.getPassword());
            user.setCredentials(Collections.singletonList(credential));

            // Insertion d'user
            Response response = usersResource.create(user);
            int status = response.getStatus();
            log.info("User creation response status: {}", status);

            switch (status) {
                case 201:
                    return new ApiResponse(201, "Utilisateur crée avec succés");
                case 409:
                    return new ApiResponse(409, "Utilisateur existe déjà");
                case 400:
                    return new ApiResponse(400, "Mot de passe ne respecte pas la policie");
                default:
                    return new ApiResponse(500, "Une erreur a été survenue, Veuillez réessayer plus tard");
            }
        } catch (Exception e) {
            log.error("Exception during user creation: ", e);
            throw new IOException("Error creating user", e);
        }
    }

    public UserDTO getUserIdByEmail(String email) throws IOException {
        List<UserRepresentation> users = usersResource.search(email);
        if (!users.isEmpty()) {
            UserRepresentation userRepresentation = users.get(0);
            return createUserDTO(userRepresentation);
        }
        return null;
    }


    public List<UserDTO> getAllUsers() throws IOException {

        List<UserRepresentation> userRepresentations = usersResource.list();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (UserRepresentation userRepresentation : userRepresentations) {
            userDTOs.add(createUserDTO(userRepresentation));
        }
        return userDTOs;
    }

    //Il peut changer email,mdp,nom,prenom

    public ApiResponse updateUser(UserDTO userDTO) throws IOException {

        return null;
    }


    public ApiResponse deleteUser(String userId) {
        usersResource.get(userId).remove();
        return new ApiResponse(200, "Utilisateur supprimé");
    }

    public ApiResponse deleteUserByEmail(String email) throws IOException {

        UserDTO userDTO = getUserIdByEmail(email);
        deleteUser(userDTO.getId());
        return new ApiResponse(200, "Utilisateur supprimé");
    }


    public UserDTO createUserDTO(UserRepresentation userRepresentation) throws IOException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userRepresentation.getId());
        userDTO.setEmail(userRepresentation.getEmail());
        userDTO.setFirstName(userRepresentation.getFirstName());
        userDTO.setLastName(userRepresentation.getLastName());
        return userDTO;
    }

    public boolean userExists(String email) {
        List<UserRepresentation> users = usersResource.search(email,true);
        return !users.isEmpty();
    }
}