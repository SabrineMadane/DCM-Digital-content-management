package com.example.service.impl;

import com.example.domain.Tag;
import com.example.dto.TagDTO;
import com.example.dto.mapper.TagMapper;
import com.example.repository.TagRepo;
import com.example.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepo tagRepository;

    @Override
    @Transactional
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = TagMapper.toEntity(tagDTO);
        Tag savedTag = tagRepository.save(tag);
        return TagMapper.toDTO(savedTag);
    }

    @Override
    @Transactional(readOnly = true)
    public TagDTO getTagById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.map(TagMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(TagMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        if (!tagRepository.existsById(id)) {
            return null; // Or throw an exception
        }
        Tag tag = TagMapper.toEntity(tagDTO);
        tag.setId(id);
        Tag updatedTag = tagRepository.save(tag);
        return TagMapper.toDTO(updatedTag);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        } else {
            // Or throw an exception
        }
    }
}
