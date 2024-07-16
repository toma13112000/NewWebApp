package org.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String fileUploadDir;

    private final String photoStorageSubDirectory = "profile-photos/";
    private final String portfolioStorageSubDirectory = "portfolio-files/";

    private Path getStoragePath(String subDirectory) {
        return Paths.get(fileUploadDir, subDirectory).toAbsolutePath().normalize();
    }

    public String saveFile(MultipartFile file, String subDirectory) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path directoryPath = getStoragePath(subDirectory);
        Path filePath = directoryPath.resolve(originalFileName).normalize();

        // Создаем директорию, если она не существует
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    public String savePhoto(MultipartFile file) throws IOException {
        return saveFile(file, photoStorageSubDirectory);
    }

    public String savePortfolioFile(MultipartFile file) throws IOException {
        return saveFile(file, portfolioStorageSubDirectory);
    }

    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

    public String updateFile(String existingFilePath, MultipartFile newFile, String subDirectory) throws IOException {
        deleteFile(existingFilePath);
        return saveFile(newFile, subDirectory);
    }

    public String updatePhoto(String existingFilePath, MultipartFile newFile) throws IOException {
        return updateFile(existingFilePath, newFile, photoStorageSubDirectory);
    }

    public String updatePortfolioFile(String existingFilePath, MultipartFile newFile) throws IOException {
        return updateFile(existingFilePath, newFile, portfolioStorageSubDirectory);
    }
}
