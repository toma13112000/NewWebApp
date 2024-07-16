package org.example.service;

import org.example.DTO.CertificateDTO;
import org.example.DTO.GraduateRegistrationDTO;
import org.example.exception.EmailAlreadyExistsForRoleException;
import org.example.exception.InvalidRequestException;
import org.example.exception.PhoneNumberAlreadyExistsForRoleException;
import org.example.model.CertificateRegistry;
import org.example.model.Graduate;
import org.example.model.Role;
import org.example.repository.GraduateRepository;
import org.example.repository.UserRepository;
import org.example.util.PhoneNumberUtils;
import org.example.enumerate.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GraduateService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PortfolioService portfolioService;

    private final GraduateRepository graduateRepository;

    @Autowired
    public GraduateService(GraduateRepository graduateRepository) {
        this.graduateRepository = graduateRepository;
    }

    @Transactional
    public Graduate registerGraduate(GraduateRegistrationDTO registrationDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidRequestException("Invalid request", new Throwable("Details of the error"));
        }

        Role role = roleService.findByType(RoleType.GRADUATE);

        String formattedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(registrationDTO.getPhoneNumber());

        if (existsByEmailAndRole(registrationDTO.getEmail(), RoleType.GRADUATE)) {
            throw new EmailAlreadyExistsForRoleException("Email already exists for this role");
        }

        if (existsByPhoneNumberAndRole(formattedPhoneNumber, RoleType.GRADUATE)) {
            throw new PhoneNumberAlreadyExistsForRoleException("Phone number already exists for this role");
        }

        // Проверяем наличие обязательных данных
        MultipartFile photoFile = registrationDTO.getPhotoFile();
        if (photoFile == null || photoFile.isEmpty() || photoFile.getSize() <= 0) {
            throw new IllegalArgumentException("Photo file is required and cannot be empty");
        }

        List<CertificateDTO> certificates = registrationDTO.getCertificates();
        if (certificates == null || certificates.isEmpty()) {
            throw new IllegalArgumentException("At least one certificate is required");
        }

        // Создаем объект выпускника
        Graduate graduate = new Graduate();
        graduate.setUsername(registrationDTO.getUsername());
        graduate.setEmail(registrationDTO.getEmail());
        graduate.setPhoneNumber(formattedPhoneNumber);
        graduate.setBirthDate(registrationDTO.getBirthDate());
        graduate.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        graduate.setRoleType(RoleType.GRADUATE);
        graduate.setEmploymentStatus(registrationDTO.getEmploymentStatus());
        graduate.setJobType(registrationDTO.getJobType());
        graduate.addRole(role);

        // Добавляем фото
        try {
            String savedPhotoFilePath = fileStorageService.savePhoto(photoFile);
            graduate.setPhotoFile(savedPhotoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving photo file", e);
        }

        // Добавляем сертификаты к выпускнику
        List<CertificateRegistry> certificateEntities = new ArrayList<>();
        certificates.forEach(certificateDTO -> {
            CertificateRegistry certificate = new CertificateRegistry();
            certificate.setGraduate(graduate);
            certificate.setCertificateNumber(certificateDTO.getCertificateNumber());
            certificate.setCertificateDate(certificateDTO.getCertificateDate());
            certificate.setSpecialty(certificateDTO.getSpecialty());
            certificateEntities.add(certificate);
        });
        graduate.setCertificates(certificateEntities);

        // Сохраняем выпускника вместе с сертификатами
        Graduate savedGraduate = graduateRepository.save(graduate);

        // Загружаем файлы портфолио
        List<MultipartFile> portfolioFiles = registrationDTO.getPortfolioFiles();
        if (portfolioFiles != null && !portfolioFiles.isEmpty()) {
            try {
                portfolioService.uploadFilesToPortfolio(savedGraduate.getId(), portfolioFiles);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error saving portfolio files", e);
            }
        }

        return savedGraduate;
    }

    @Transactional
    public void uploadPhoto(Long graduateId, MultipartFile file) throws IOException {
        Graduate graduate = graduateRepository.findById(graduateId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid graduate ID"));

        String photoFilePath = fileStorageService.savePhoto(file);
        graduate.setPhotoFile(photoFilePath);
        graduateRepository.save(graduate);
    }

    @Transactional
    public void updatePhoto(Long graduateId, MultipartFile photo) throws IOException {
        Graduate graduate = graduateRepository.findById(graduateId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid graduate ID"));

        String photoFilePath = fileStorageService.savePhoto(photo);
        graduate.setPhotoFile(photoFilePath);
        graduateRepository.save(graduate);
    }

    public void addCertificate(Graduate graduate, CertificateDTO certificateDTO) {
        CertificateRegistry certificate = new CertificateRegistry();
        certificate.setGraduate(graduate);
        certificate.setCertificateNumber(certificateDTO.getCertificateNumber());
        certificate.setCertificateDate(certificateDTO.getCertificateDate());
        certificate.setSpecialty(certificateDTO.getSpecialty());

        certificateService.addCertificate(certificate);
    }

    public void removeCertificate(Long certificateId) {
        certificateService.removeCertificate(certificateId);
    }

    public void updateCertificate(CertificateRegistry updatedCertificate) {
        certificateService.updateCertificate(updatedCertificate);
    }

    public boolean existsByEmailAndRole(String email, RoleType type) {
        return userRepository.existsByEmailAndRoles_Type(email, type);
    }

    public boolean existsByPhoneNumberAndRole(String phoneNumber, RoleType type) {
        return userRepository.existsByPhoneNumberAndRoles_Type(phoneNumber, type);
    }

    public void deleteByPhoneNumber(String phoneNumber) {
        userRepository.deleteByPhoneNumber(phoneNumber);
    }

    public List<Graduate> findAll() {
        return graduateRepository.findAll();
    }

    public Optional<Graduate> findById(Long id) {
        return graduateRepository.findById(id);
    }

    public void deleteById(Long id) {
        graduateRepository.deleteById(id);
    }

    public void uploadPortfolio(Long graduateId, List<MultipartFile> files) throws IOException {
        portfolioService.uploadFilesToPortfolio(graduateId, files);
    }

    public Map<String, Boolean> checkUserExists(String phoneNumber, String email) {
        String formattedPhoneNumber = PhoneNumberUtils.formatPhoneNumber(phoneNumber);
        boolean phoneExists = existsByPhoneNumberAndRole(formattedPhoneNumber, RoleType.GRADUATE);
        boolean emailExists = existsByEmailAndRole(email, RoleType.GRADUATE);
        return Map.of("phoneExists", phoneExists, "emailExists", emailExists);
    }

    public List<Graduate> findGraduatesBySpecialty(String specialty) {
        return graduateRepository.findByCertificateRegistryList_SpecialtyContaining(specialty);
    }
}
