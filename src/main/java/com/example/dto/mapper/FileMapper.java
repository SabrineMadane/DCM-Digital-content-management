package com.example.dto.mapper;

import com.example.domain.File;
import com.example.domain.Statut;
import com.example.dto.FileDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class FileMapper {

    public static FileDTO toDTO(File file) {
        if (file == null) {
            return null;
        }

        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(file.getId());
        fileDTO.setFileName(file.getFileName());
        fileDTO.setDescription(file.getDescription());
        fileDTO.setType(file.getType());
        fileDTO.setChemin(file.getChemin());
        fileDTO.setStatut(file.getStatut().name()); // Convertit l'énumération en String
        fileDTO.setUserId(file.getUser() != null ? file.getUser().getId() : null);

        // Convertit les tags en IDs
        Set<Long> tagIds = file.getTags().stream()
                .map(tag -> tag.getId())
                .collect(Collectors.toSet());
        fileDTO.setTagIds(tagIds);

        return fileDTO;
    }

    public static File toEntity(FileDTO fileDTO) {
        if (fileDTO == null) {
            return null;
        }

        File file = new File();
        file.setId(fileDTO.getId());
        file.setFileName(fileDTO.getFileName());
        file.setDescription(fileDTO.getDescription());
        file.setType(fileDTO.getType());
        file.setChemin(fileDTO.getChemin());
        file.setStatut(Statut.valueOf(fileDTO.getStatut())); // Convertit le String en énumération
        // Note: Vous devrez peut-être récupérer l'objet Utilisateur à partir de son ID
        // Note: Vous devrez peut-être récupérer les objets Tag à partir de leurs IDs

        return file;
    }
}
