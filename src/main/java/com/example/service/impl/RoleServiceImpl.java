package com.example.service.impl;

import com.example.domain.Role;
import com.example.dto.RoleDTO;
import com.example.dto.mapper.RoleMapper;
import com.example.repository.RoleRepo;
import com.example.service.RoleService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepo roleRepository;

    @Autowired
    private RolesResource rolesResource;

    @Autowired
    private GroupsResource groupsResource;

    @Autowired
    private ClientsResource clientsResource;

    @Autowired
    private String clientUUID;

    @Override
    public RoleDTO addRole(RoleDTO roleDTO) {
        try {
            // 1. Créer un groupe dans Keycloak avec le même nom que le RoleDTO
            GroupRepresentation groupRepresentation = new GroupRepresentation();
            groupRepresentation.setName(roleDTO.getNom());
            groupsResource.add(groupRepresentation);

            // 2. Récupérer le groupe nouvellement créé
            GroupRepresentation createdGroup = groupsResource.groups().stream()
                    .filter(g -> g.getName().equals(roleDTO.getNom()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Group not found after creation"));

            // 3. Parcourir les permissions et associer les rôles correspondants
            associateClientRolesWithPermissions(createdGroup.getId(), roleDTO.getLibraryPermissions());
            associateClientRolesWithPermissions(createdGroup.getId(), roleDTO.getGestionPermissions());

            // 4. Créer le rôle dans la base de données locale
            Role role = RoleMapper.toEntity(roleDTO);
            roleRepository.save(role);

            log.info("Role successfully created and associated with group: {}", role.getNom());
            return RoleMapper.toDTO(role);
        } catch (Exception e) {
            log.error("Error creating role and group: ", e);
            throw new RuntimeException("Error creating role and group", e);
        }
    }


    private void associateClientRolesWithPermissions(String groupId, Map<String, Boolean> permissions) {
        ClientResource clientResource = clientsResource.get(clientUUID);

        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                String roleName = entry.getKey();  // Le nom du rôle correspond au nom de la permission
                RoleRepresentation roleRepresentation = clientResource.roles().get(roleName).toRepresentation();
                groupsResource.group(groupId).roles().clientLevel(clientUUID).add(Collections.singletonList(roleRepresentation));
            }
        }
    }


    @Override
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            return RoleMapper.toDTO(role);
        }
        return null;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(RoleMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO) {
        try {
            // Update role in the local database
            Role role = roleRepository.findById(roleDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            role.setNom(roleDTO.getNom());
            role.setDescription(roleDTO.getDescription());
            roleRepository.save(role);

            // Update role in Keycloak
            RoleRepresentation roleRepresentation = rolesResource.get(roleDTO.getNom()).toRepresentation();
            roleRepresentation.setName(roleDTO.getNom());
            roleRepresentation.setDescription(roleDTO.getDescription());
            rolesResource.get(roleDTO.getNom()).update(roleRepresentation);

            log.info("Role successfully updated: {}", role.getNom());
            return RoleMapper.toDTO(role);
        } catch (Exception e) {
            log.error("Error updating role: ", e);
            throw new RuntimeException("Error updating role", e);
        }
    }

    @Override
    public void deleteRole(Long id) {
        try {
            Role role = roleRepository.findById(id).orElse(null);
            if (role != null) {
                // Delete role in Keycloak
                rolesResource.get(role.getNom()).remove();

                // Delete role in the local database
                roleRepository.deleteById(id);

                log.info("Role successfully deleted: {}", role.getNom());
            } else {
                throw new RuntimeException("Role not found");
            }
        } catch (Exception e) {
            log.error("Error deleting role: ", e);
            throw new RuntimeException("Error deleting role", e);
        }
    }

    @Override
    public boolean roleExists(String nom) {
        return roleRepository.existsByNom(nom);
    }
}
