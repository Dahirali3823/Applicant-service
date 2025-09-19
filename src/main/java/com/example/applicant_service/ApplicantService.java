package com.example.applicant_service;

import com.example.applicant_service.Applicant;
import com.example.applicant_service.ApplicantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ApplicantService {
    private final ApplicantRepository repository;

    public ApplicantService(ApplicantRepository Repository) {
        this.repository = Repository;
    }
    public Applicant save(Applicant applicant) {
        return repository.save(applicant);
    }
    public List<Applicant> findAll() {
        return repository.findAll();
    }
    public Applicant findById(Long id) {
        return repository.findById(id).orElse(null);
    }
    public Applicant findByName(String name) {
        return repository.findByName(name).orElse(null);
    }
    public void delete(Long ID) {
        repository.deleteById(ID);
    }
    public void deleteAll(){
        repository.deleteAll();
    }



}
