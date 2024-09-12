package com.example.service.impl;

import com.example.config.FileStorageService;
import com.example.domain.File;
import com.example.domain.Tag;
import com.example.domain.Utilisateur;
import com.example.dto.FileDTO;

import com.example.dto.mapper.FileMapper;
import com.example.repository.FileRepo;
import com.example.repository.TagRepo;
import com.example.repository.UtilisateurRepo;
import com.example.service.FileService;

import org.keycloak.admin.client.resource.ClientsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepo fileRepository;

    @Autowired
    private UtilisateurRepo utilisateurRepo;

    @Autowired
    private TagRepo tagRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ClientsResource clientsResource;

    @Autowired
    private String clientUUID;

    @Override
    @Transactional
    public FileDTO createFile(FileDTO fileDTO, MultipartFile file) throws Exception {
        // Log the incoming fileDTO
        System.out.println("Received FileDTO: " + fileDTO);
        System.out.println("User ID: " + fileDTO.getUserId());

        // Upload to MinIO
        String bucketName = "buckett";
        String chemin = fileDTO.getChemin();
        String objectName = chemin + fileDTO.getFileName();
        fileStorageService.uploadFile(bucketName, objectName, file);

        // Retrieve User entity
        Utilisateur user = utilisateurRepo.findById(fileDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert DTO to entity
        File fileEntity = FileMapper.toEntity(fileDTO);
        fileEntity.setChemin(chemin);
        fileEntity.setUser(user); // Set the user field

        // Handle tags
        Set<Tag> tags = new HashSet<>();
        for (Long tagId : fileDTO.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId));
            tags.add(tag);
        }
        fileEntity.setTags(tags);

        // Save file info to database
        File savedFile = fileRepository.save(fileEntity);
        return FileMapper.toDTO(savedFile);
    }


    @Override
    @Transactional(readOnly = true)
    public FileDTO getFileById(Long id) {
        Optional<File> file = fileRepository.findById(id);
        return file.map(FileMapper::toDTO).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> getAllFiles() {
        List<File> files = fileRepository.findAll();
        return files.stream().map(FileMapper::toDTO).collect(Collectors.toList());
    }

    // Ajouter dans FileServiceImpl

    @Override
    public String getVideoUrl(String bucketName, String objectName) throws Exception {
        return fileStorageService.getPresignedUrl(bucketName, objectName);
    }



    @Transactional
    public FileDTO updateFile(Long id, FileDTO fileDTO, MultipartFile file) throws Exception {
        if (!fileRepository.existsById(id)) {
            return null; // Or throw an exception
        }
        // Upload to MinIO
        String bucketName = "buckett";
        String chemin = fileDTO.getChemin();
        String objectName = chemin + fileDTO.getFileName();
        fileStorageService.uploadFile(bucketName, objectName, file);

        // Update file info in database
        File fileEntity = FileMapper.toEntity(fileDTO);
        fileEntity.setId(id);
        fileEntity.setChemin(chemin);
        File updatedFile = fileRepository.save(fileEntity);
        return FileMapper.toDTO(updatedFile);
    }

    @Override
    @Transactional
    public void deleteFile(Long id) throws Exception {
        Optional<File> fileOpt = fileRepository.findById(id);
        if (fileOpt.isPresent()) {
            File file = fileOpt.get();
            String bucketName = "your-bucket-name";
            String objectName = file.getChemin() + file.getFileName();

            // Delete from MinIO
            fileStorageService.deleteFile(bucketName, objectName);

            // Delete from database
            fileRepository.deleteById(id);
        } else {
            // Or throw an exception
        }
    }
}
