package com.example.applicant_service;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applicants")
@CrossOrigin("http://localhost:5173")
public class ApplicantController {
    private final ApplicantService service;
    public ApplicantController(ApplicantService service) {
        this.service = service;
    }

    // create a new applicant
    @PostMapping
    public ResponseEntity<Applicant> create(@RequestBody Applicant applicant) {
        System.out.println("Received applicant: " + applicant);

        Applicant saved = service.save(applicant);
        return ResponseEntity.ok(saved);
    }
    // update or create an applicant completely
    @PutMapping("/id/{id}")
    public ResponseEntity<Applicant> update(@PathVariable Long id,@RequestBody Applicant applicant) {
        System.out.println("Received applicant: " + applicant);
        Applicant saved = service.save(applicant);
        return ResponseEntity.ok(saved);
    }
    // update parts of an applicant
    @PatchMapping("id/{id}")
    public ResponseEntity<Applicant> patch(@PathVariable Long id,@RequestBody Map<String, Object> updates) {
        Applicant applicant = service.findById(id);
        if(applicant == null) {
            return ResponseEntity.notFound().build();
        }
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    applicant.setName((String) value);
                    break;
                case "age":
                    applicant.setAge(((Number) value).intValue());
                    break;
                case "income":
                    applicant.setIncome(((Number) value).doubleValue());
                    break;
                case "debt":
                    applicant.setDebt(((Number) value).doubleValue());
                    break;
                case "creditScore":
                    applicant.setCreditScore(((Number) value).intValue());
                    break;
                case "loanAmount":
                    applicant.setLoanAmount(((Number) value).doubleValue());
                    break;
                case "monthsEmployed":
                    applicant.setMonthsEmployed(((Number) value).intValue());
                    break;
                case "residentMonths":
                    applicant.setResidentMonths(((Number) value).intValue());
                    break;
                case "monthlyDebtPayments":
                    applicant.setMonthlyDebtPayments(((Number) value).doubleValue());
                    break;
            }
        });

        Applicant saved = service.save(applicant);
        return ResponseEntity.ok(saved);
    }
    // get all applicants in system
    @GetMapping
    public ResponseEntity<List<Applicant>> getAll() {
        System.out.println("Received list of applicants: "+ service.findAll());
        return ResponseEntity.ok(service.findAll());
    }
    // get a specific applicant using id
    @GetMapping("/id/{id}")
    public ResponseEntity<Applicant> getApplicant(@PathVariable Long id) {
        System.out.println("Received list of applicants: "+ service.findById(id));
        if(service.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findById(id));
    }
    // get applicant by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Applicant> getApplicantByName(@PathVariable String name) {
        if(service.findByName(name) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.findByName(name));
    }
    // delete applicant by id
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    // delete all applicants
    @DeleteMapping("/delete-all")
    public ResponseEntity<Void> deleteAll() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }



}
