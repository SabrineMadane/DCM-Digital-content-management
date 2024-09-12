package com.example.service;

import com.example.dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO createTag(TagDTO tagDTO);

    List<TagDTO> getAllTags();

    TagDTO getTagById(Long id);

    TagDTO updateTag(Long id, TagDTO updatedTagDTO);

    void deleteTag(Long id);
}
