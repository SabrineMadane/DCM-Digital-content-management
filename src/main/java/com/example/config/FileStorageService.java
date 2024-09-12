package com.example.config;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class FileStorageService {

    @Autowired
    private MinioClient minioClient;

    public void uploadFile(String bucketName, String objectName, MultipartFile file) throws Exception {
        try (InputStream fileStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(fileStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new Exception("Error occurred while uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    public InputStream downloadFile(String bucketName, String objectName) throws Exception {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new Exception("Error occurred while downloading file from MinIO: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String bucketName, String objectName) throws Exception {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new Exception("Error occurred while deleting file from MinIO: " + e.getMessage(), e);
        }
    }
    // Nouvelle méthode pour obtenir une URL signée pour accéder à un fichier
    public String getPresignedUrl(String bucketName, String objectName) throws Exception {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(2, TimeUnit.HOURS)  // L'URL sera valide pendant 2 heures
                            .build()
            );
        } catch (Exception e) {
            throw new Exception("Error occurred while generating presigned URL: " + e.getMessage(), e);
        }
    }
}
