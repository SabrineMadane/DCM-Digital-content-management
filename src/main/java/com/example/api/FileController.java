package com.example.api;

import com.example.config.ApiResponse;
import com.example.dto.FileDTO;
import com.example.service.impl.FileServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import com.example.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Validated
@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FileController {

    @Autowired
    private FileService fileService;



    @PostMapping
    public ResponseEntity<FileDTO> createFile(@RequestParam("file") MultipartFile file, @RequestParam("fileInfo") String fileInfo) throws Exception {
        FileDTO fileDTO = new ObjectMapper().readValue(fileInfo, FileDTO.class);
        FileDTO createdFile = fileService.createFile(fileDTO, file);
        return new ResponseEntity<>(createdFile, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDTO> getFileById(@PathVariable Long id) {
        FileDTO fileDTO = fileService.getFileById(id);
        if (fileDTO != null) {
            return new ResponseEntity<>(fileDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/url")
    public ResponseEntity<List<FileDTO>> getAllVideos() {
        List<FileDTO> files = fileService.getAllFiles();

        // Ajouter les URLs des vidéos
        for (FileDTO file : files) {
            try {
                String videoUrl = fileService.getVideoUrl("buckett", file.getChemin() + file.getFileName());
                file.setUrl(videoUrl);
            } catch (Exception e) {
                e.printStackTrace();
                // Vous pouvez également choisir de retourner une réponse d'erreur ici
            }
        }
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FileDTO>> getAllFiles() {
        List<FileDTO> files = fileService.getAllFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) throws Exception {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}