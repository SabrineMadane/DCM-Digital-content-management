package com.example.service;

import com.example.dto.FileDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {


    @Transactional
    FileDTO createFile(FileDTO fileDTO, MultipartFile file) throws Exception;

    @Transactional(readOnly = true)
    FileDTO getFileById(Long id);

    @Transactional(readOnly = true)
    List<FileDTO> getAllFiles();


    String getVideoUrl(String bucketName, String objectName) throws Exception;

    @Transactional
    void deleteFile(Long id) throws Exception;
}
