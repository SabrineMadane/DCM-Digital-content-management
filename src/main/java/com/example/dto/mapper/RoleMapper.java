package com.example.dto.mapper;

import com.example.domain.GestionPermission;
import com.example.domain.LibraryPermission;
import com.example.domain.Role;
import com.example.dto.RoleDTO;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setNom(role.getNom());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setEtat(role.getEtat());

        // Convertit les permissions en maps avec les noms des permissions comme clés
        Map<String, Boolean> libraryPermissions = role.getLibraryPermissions().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));
        roleDTO.setLibraryPermissions(libraryPermissions);

        Map<String, Boolean> gestionPermissions = role.getGestionPermissions().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue));
        roleDTO.setGestionPermissions(gestionPermissions);

        // Convertit les utilisateurs en IDs
        Set<Long> userIds = role.getUsers().stream()
                .map(user -> user.getId())
                .collect(Collectors.toSet());
        roleDTO.setUserIds(userIds);

        return roleDTO;
    }

    public static Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }

        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setNom(roleDTO.getNom());
        role.setDescription(roleDTO.getDescription());
        role.setEtat(roleDTO.getEtat());

        // Convertit les maps de permissions en maps d'énumérations
        Map<LibraryPermission, Boolean> libraryPermissions = roleDTO.getLibraryPermissions().entrySet().stream()
                .collect(Collectors.toMap(e -> LibraryPermission.valueOf(e.getKey()), Map.Entry::getValue));
        role.setLibraryPermissions(libraryPermissions);

        Map<GestionPermission, Boolean> gestionPermissions = roleDTO.getGestionPermissions().entrySet().stream()
                .collect(Collectors.toMap(e -> GestionPermission.valueOf(e.getKey()), Map.Entry::getValue));
        role.setGestionPermissions(gestionPermissions);

        // Note: Vous devrez peut-être récupérer les utilisateurs à partir de leurs IDs

        return role;
    }
}
