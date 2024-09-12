package com.example.api;


import com.example.config.ApiResponse;
import com.example.dto.UtilisateurDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.service.impl.UtilisateurServiceImpl;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UtilisateurController {

    @Autowired
    private UtilisateurServiceImpl utilisateurService;


    @PreAuthorize("hasRole('role_test')")
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UtilisateurDTO utilisateurDTO) {
        // Ajouter cette ligne pour déboguer les autorités de l'utilisateur authentifié
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        System.out.println("les autorisationnnnnnnnnnnnnnnnn");
        authorities.forEach(authority -> System.out.println(authority.getAuthority()));
        System.out.println("lfu9");

        try {
            ApiResponse response = utilisateurService.addUser(utilisateurDTO);
            HttpStatus status = response.getStatus() != null ? response.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, status);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(500, "Erreur lors de la création de l'utilisateur"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUserById(@PathVariable Long id) {
        try {
            UtilisateurDTO utilisateurDTO = utilisateurService.getUserById(id);
            if (utilisateurDTO != null) {
                return new ResponseEntity<>(utilisateurDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUsers() {
        try {
            List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUsers();
            return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('modifier_user')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        try {
            utilisateurDTO.setId(id);
            ApiResponse response = utilisateurService.updateUser(utilisateurDTO);
            HttpStatus status = response.getStatus() != null ? response.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, status);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(500, "Erreur lors de la mise à jour de l'utilisateur"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PreAuthorize("hasRole('supprimer_user')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        ApiResponse response = utilisateurService.deleteUser(id);
        HttpStatus status = response.getStatus() != null ? response.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(response, status);
    }

    @PreAuthorize("hasRole('supprimer_user')")
    @DeleteMapping("/email/{email}")
    public ResponseEntity<ApiResponse> deleteUserByEmail(@PathVariable String email) {
        try {
            ApiResponse response = utilisateurService.deleteUserByEmail(email);
            HttpStatus status = response.getStatus() != null ? response.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, status);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(500, "Erreur lors de la suppression de l'utilisateur"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> userExists(@PathVariable String email) {
        boolean exists = utilisateurService.userExists(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
