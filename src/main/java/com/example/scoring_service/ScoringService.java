package com.example.scoring_service;
import com.example.applicant_service.*;

import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    private final ApplicantService applicantService;

    private final CreditScore creditScore;

    public ScoringService(CreditScore creditScore, ApplicantService applicantService) {
        this.creditScore = creditScore;
        this.applicantService = applicantService;
    }

    public int calculateScore(Long applicantId) {
        Applicant applicant = applicantService.findById(applicantId);
        return creditScore.calculateScore(applicant);

    }
    public String explanation(Long applicantId) {
        Applicant applicant = applicantService.findById(applicantId);
        return creditScore.getExplanation(applicant);
    }
}