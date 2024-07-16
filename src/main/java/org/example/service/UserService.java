package org.example.service;

import org.example.DTO.*;
import org.example.enumerate.RoleType;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final GraduateRepository graduateRepository;
    private final RoleRepository roleRepository;
    private final RecruitingCompanyRepository recruitingCompanyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    public UserService(UserRepository userRepository, EmployerRepository employerRepository,
                       GraduateRepository graduateRepository, RoleRepository roleRepository,
                       RecruitingCompanyRepository recruitingCompanyRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employerRepository = employerRepository;
        this.graduateRepository = graduateRepository;
        this.roleRepository = roleRepository;
        this.recruitingCompanyRepository = recruitingCompanyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmailAndRole(String email, RoleType roleType) {
        return userRepository.existsByEmailAndRoles_Type(email, roleType);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhoneNumberAndRole(String phoneNumber, RoleType roleType) {
        return userRepository.existsByPhoneNumberAndRoles_Type(phoneNumber, roleType);
    }

    @Transactional
    public void registerGraduate(GraduateRegistrationDTO registrationDTO) {
        if (existsByEmailAndRole(registrationDTO.getEmail(), RoleType.GRADUATE)) {
            throw new IllegalArgumentException("Email is already in use for this role");
        }
        if (existsByPhoneNumberAndRole(registrationDTO.getPhoneNumber(), RoleType.GRADUATE)) {
            throw new IllegalArgumentException("Phone number is already in use for this role");
        }
        if (!registrationDTO.getPassword().equals(registrationDTO.getPassword2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Graduate graduate = new Graduate();
        graduate.setUsername(registrationDTO.getUsername());
        graduate.setEmail(registrationDTO.getEmail());
        graduate.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        Role graduateRole = roleRepository.findByType(RoleType.GRADUATE);
        if (graduateRole != null) {
            graduate.addRole(graduateRole);
        } else {
            throw new IllegalArgumentException("Role GRADUATE not found");
        }

        graduate.setBirthDate(registrationDTO.getBirthDate());
        graduate.setPhoneNumber(registrationDTO.getPhoneNumber());
        graduate.setRoleType(RoleType.GRADUATE);
        graduate.setEmploymentStatus(registrationDTO.getEmploymentStatus());
        graduate.setJobType(registrationDTO.getJobType());

        Graduate savedGraduate = graduateRepository.save(graduate);

        // Обработка сертификатов
        List<CertificateDTO> certificates = registrationDTO.getCertificates();
        if (certificates != null && !certificates.isEmpty()) {
            certificates.forEach(certificate -> {
                CertificateRegistry certificateRegistry = new CertificateRegistry();
                certificateRegistry.setCertificateNumber(certificate.getCertificateNumber());
                certificateRegistry.setCertificateDate(certificate.getCertificateDate());
                certificateRegistry.setSpecialty(certificate.getSpecialty());
                certificateRegistry.setGraduate(savedGraduate);
                certificateService.addCertificate(certificateRegistry);
            });
        }

        // Обработка фото
        MultipartFile photoFile = registrationDTO.getPhotoFile();
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                String photoPath = fileStorageService.savePhoto(photoFile); // Use a different variable name here
                savedGraduate.updatePhoto(photoPath); // Update using the correct variable name
                graduateRepository.save(savedGraduate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        // Обработка файлов портфолио
        List<MultipartFile> portfolioFiles = registrationDTO.getPortfolioFiles();
        if (portfolioFiles != null && !portfolioFiles.isEmpty()) {
            try {
                portfolioService.uploadFilesToPortfolio(savedGraduate.getId(), portfolioFiles);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Transactional
    public RecruitingCompany registerRecruitingCompany(RecruitingCompanyRegistrationDTO registrationDTO) {
        if (existsByEmailAndRole(registrationDTO.getEmail(), RoleType.RECRUITING_COMPANY)) {
            throw new IllegalArgumentException("Email is already in use for this role");
        }
        if (existsByPhoneNumberAndRole(registrationDTO.getPhoneNumber(), RoleType.RECRUITING_COMPANY)) {
            throw new IllegalArgumentException("Phone number is already in use for this role");
        }

        RecruitingCompany recruitingCompany = new RecruitingCompany();
        recruitingCompany.setUsername(registrationDTO.getUsername());
        recruitingCompany.setEmail(registrationDTO.getEmail());
        recruitingCompany.setPhoneNumber(registrationDTO.getPhoneNumber().replaceAll("\\D", ""));
        recruitingCompany.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        recruitingCompany.setRoleType(RoleType.RECRUITING_COMPANY);

        return recruitingCompanyRepository.save(recruitingCompany);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional
    public void updateUserPasswordByPhone(String phone, String encodedPassword) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phone);
        userOptional.ifPresent(user -> {
            user.setPassword(encodedPassword);
            userRepository.save(user);
        });
    }

    @Transactional
    public void setLastLoginRole(String email, String role) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setLastLoginRole(role);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public List<Graduate> getGraduates() {
        return graduateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Employer> getEmployers() {
        return employerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RecruitingCompany> getRecruitingCompany() {
        return recruitingCompanyRepository.findAll();
    }

    @Transactional
    public void registerUser(UserRegistrationDTO registrationDTO) {
        if (existsByEmailAndRole(registrationDTO.getEmail(), registrationDTO.getRole())) {
            throw new IllegalArgumentException("Email is already in use for this role");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPhoneNumber(registrationDTO.getPhoneNumber().replaceAll("\\D", ""));
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        Role role = roleRepository.findByType(registrationDTO.getRole());
        if (role != null) {
            user.addRole(role);
        } else {
            throw new IllegalArgumentException("Role not found");
        }

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findSimilarUsersBySpecialty(String specialty) {
        List<Graduate> graduates = graduateRepository.findByCertificateRegistryList_SpecialtyContaining(specialty);
        return graduates.stream().map(graduate -> (User) graduate).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public Employer registerEmployer(EmployerRegistrationDTO registrationDTO) {
        if (existsByEmailAndRole(registrationDTO.getEmail(), RoleType.EMPLOYER)) {
            throw new IllegalArgumentException("Email is already in use for this role");
        }
        if (existsByPhoneNumberAndRole(registrationDTO.getPhoneNumber(), RoleType.EMPLOYER)) {
            throw new IllegalArgumentException("Phone number is already in use for this role");
        }

        Employer employer = new Employer();
        employer.setEmail(registrationDTO.getEmail());
        employer.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        Role employerRole = roleRepository.findByType(RoleType.EMPLOYER);
        if (employerRole != null) {
            employer.addRole(employerRole);
        } else {
            throw new IllegalArgumentException("Role EMPLOYER not found");
        }

        employer.setPhoneNumber(registrationDTO.getPhoneNumber());
        employer.setRoleType(RoleType.EMPLOYER);
        employer.setCompanyName(registrationDTO.getCompanyName());
        employer.setCompanyUrl(registrationDTO.getCompanyUrl());
        employer.setCompanyActivity(registrationDTO.getCompanyActivity());

        employerRepository.save(employer);
        return employer;
    }

    @Transactional
    public boolean changeUserPassword(String phoneNumber, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Double getRatingForGraduate(Long graduateId) {
        Graduate graduate = graduateRepository.findById(graduateId).orElse(null);
        if (graduate != null) {
            return graduate.getRating();
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}
