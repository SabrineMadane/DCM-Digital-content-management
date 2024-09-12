package com.example.repository;

import com.example.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UtilisateurRepo extends JpaRepository<Utilisateur,Long> {
    Utilisateur findByEmail(String email);
    boolean existsByEmail(String email);


}
