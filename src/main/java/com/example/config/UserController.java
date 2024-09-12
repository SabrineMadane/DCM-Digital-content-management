package com.example.config;



import com.example.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/com/example/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(KeycloakServiceImpl.class);


    @Autowired
    private KeycloakServiceImpl keycloakUserService;

    @PostMapping
    public ApiResponse createUser(@RequestBody UserDTO userDTO) throws IOException {
        return keycloakUserService.addUser(userDTO);
    }

    @GetMapping("/all")
    public ApiResponse getAllUsers() {
        try {
            List<UserDTO> users = keycloakUserService.getAllUsers();

            // Imprimer les informations des utilisateurs dans la console
            for (UserDTO user : users) {
                log.info("User ID: {}", user.getId());
                log.info("User Email: {}", user.getEmail());
                log.info("User First Name: {}", user.getFirstName());
                log.info("User Last Name: {}", user.getLastName());
                log.info("------------------------");
            }

            // Répondre avec un message de succès
            return new ApiResponse(200, "Les informations des utilisateurs ont été imprimées dans la console.");
        } catch (IOException e) {
            log.error("Erreur lors de la récupération des utilisateurs", e);
            return new ApiResponse(500, "Une erreur est survenue lors de la récupération des utilisateurs.");
        }
    }
}
