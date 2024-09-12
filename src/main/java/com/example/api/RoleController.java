package com.example.api;

import com.example.dto.RoleDTO;
import com.example.service.RoleService;
import com.example.config.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO createdRole = roleService.addRole(roleDTO);
            return new ResponseEntity<>(new ApiResponse(201, "Role created successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(500, "Error creating role"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO roleDTO = roleService.getRoleById(id);
        if (roleDTO != null) {
            return new ResponseEntity<>(roleDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        try {
            roleDTO.setId(id);
            RoleDTO updatedRole = roleService.updateRole(roleDTO);
            return new ResponseEntity<>(new ApiResponse(200, "Role updated successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(500, "Error updating role"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return new ResponseEntity<>(new ApiResponse(200, "Role deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(500, "Error deleting role"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exists/{nom}")
    public ResponseEntity<Boolean> roleExists(@PathVariable String nom) {
        boolean exists = roleService.roleExists(nom);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
