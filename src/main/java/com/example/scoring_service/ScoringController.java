package com.example.scoring_service;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/scoring")
public class ScoringController {
    private final ScoringService scoringService;

    public ScoringController(ScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @GetMapping("/calculate/{applicantId}")
    public int calculateScore(@PathVariable Long applicantId) {
        return scoringService.calculateScore(applicantId);

    }
    @GetMapping("/explanation/{applicantId}")
    public String explanation(@PathVariable Long applicantId) {
        return scoringService.explanation(applicantId);
    }

    @GetMapping("/health")
    public String health() {
        return "Scoring Service is running!";
    }




}
