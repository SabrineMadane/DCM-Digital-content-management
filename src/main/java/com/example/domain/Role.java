package com.example.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ou GenerationType.AUTO
    @Column(name = "id")
    private Long id;
    private String nom;
    private String description;
    private Boolean Etat;
    @ElementCollection
    @CollectionTable(name = "library_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "permission_value")
    private Map <LibraryPermission, Boolean> libraryPermissions;
    @ElementCollection
    @CollectionTable(name = "gestion_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "permission_value")
    private Map <GestionPermission, Boolean> gestionPermissions;
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Utilisateur> users = new HashSet<>();
}
