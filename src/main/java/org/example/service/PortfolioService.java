package org.example.service;

import org.example.model.Graduate;
import org.example.model.PortfolioFile;
import org.example.repository.GraduateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private GraduateRepository graduateRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public void uploadFilesToPortfolio(Long graduateId, List<MultipartFile> files) throws IOException {
        Graduate graduate = graduateRepository.findById(graduateId)
                .orElseThrow(() -> new IllegalArgumentException("Выпускник с id " + graduateId + " не найден"));

        for (MultipartFile file : files) {
            String filePath = fileStorageService.savePortfolioFile(file);
            String fileName = file.getOriginalFilename();

            PortfolioFile portfolioFile = new PortfolioFile();
            portfolioFile.setFileName(fileName);
            portfolioFile.setFilePath(filePath);

            graduate.addToPortfolio(portfolioFile);
        }

        graduateRepository.save(graduate);
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }
}
