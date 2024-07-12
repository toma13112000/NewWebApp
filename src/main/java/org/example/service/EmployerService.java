package org.example.service;

import org.example.model.Employer;
import org.example.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployerService {

    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public Employer save(Employer employer) {
        return employerRepository.save(employer);
    }

    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    public Optional<Employer> findById(Long id) {
        return employerRepository.findById(id);
    }

    public void deleteById(Long id) {
        employerRepository.deleteById(id);
    }
}
