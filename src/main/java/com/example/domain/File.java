package com.example.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Utiliser un

    private String uuid; // Champ UUID pour la correspondance avec MinIO

    private String fileName;
    private String description;
    private String type;
    private String chemin;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Utilisateur user;


    @ManyToMany
    @JoinTable(
            name = "file_tag",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


}
