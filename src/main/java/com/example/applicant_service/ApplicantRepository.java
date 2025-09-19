package com.example.applicant_service;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Optional<Applicant> findByName(String name);

}
