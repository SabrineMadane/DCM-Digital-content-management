package com.example.domain;


import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "KEYCLOAK_ID", unique = true)
    private String keycloakId;

    @Column(name = "FIRST_NAME")
    private String nom;

    @Column(name = "LAST_NAME")
    private String prenom;

    private String fonction;

    @Column(name = "EMAIL", unique = true)
    private String email;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<File> files;
}
