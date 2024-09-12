package com.example.service;

import com.example.domain.Role;
import com.example.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO addRole(RoleDTO roleDTO);
    RoleDTO getRoleById(Long id);
    List<RoleDTO> getAllRoles();
    RoleDTO updateRole(RoleDTO roleDTO);
    void deleteRole(Long id);
    boolean roleExists(String nom);
}
