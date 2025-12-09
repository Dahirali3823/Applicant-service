package com.example.loan_service;

import com.example.applicant_service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/loans")
@CrossOrigin("http://localhost:5173")
public class LoanController{
    private final ApplicantService service;
    private final LoanDecisionService loanDecisionService;

    public LoanController(ApplicantService service, LoanDecisionService loanDecisionService) {
        this.service = service;
        this.loanDecisionService = loanDecisionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDecision> evaluateLoan(@PathVariable Long id) {
        System.out.println("Evaluating loan");
        Applicant applicant = service.findById(id);
        if(applicant == null){
            return ResponseEntity.notFound().build();
        }
        LoanDecision decision = loanDecisionService.evaluateLoan(applicant);
        return ResponseEntity.ok(decision);
    }


}
