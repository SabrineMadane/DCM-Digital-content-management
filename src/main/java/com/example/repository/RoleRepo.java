package com.example.repository;

import com.example.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepo  extends JpaRepository<Role,Long> {
    boolean existsByNom(String nom);
}

