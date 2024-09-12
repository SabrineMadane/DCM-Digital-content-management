package com.example.dto.mapper;

import com.example.domain.Tag;
import com.example.dto.TagDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class TagMapper {

    public static TagDTO toDTO(Tag tag) {
        if (tag == null) {
            return null;
        }

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setNom(tag.getNom());

        // Convertit les fichiers en IDs
        Set<Long> fileIds = tag.getFiles().stream()
                .map(file -> file.getId())
                .collect(Collectors.toSet());
        tagDTO.setFileIds(fileIds);

        return tagDTO;
    }

    public static Tag toEntity(TagDTO tagDTO) {
        if (tagDTO == null) {
            return null;
        }

        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setNom(tagDTO.getNom());

        // Note: Vous devrez peut-être récupérer les fichiers à partir de leurs IDs
        // Exemple : tag.setFiles(fileRepository.findAllById(tagDTO.getFileIds()));

        return tag;
    }
}
