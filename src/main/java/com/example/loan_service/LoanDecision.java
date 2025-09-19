package com.example.loan_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDecision {
    private Long applicantId;
    private int calculatedScore;
    private int actualCreditScore;
    private boolean approved;
    private double interestRate;
    private double maxLoanAmount;
    private double requestedAmount;
    private double monthlyPayment;
    private double totalDTI;
    private String riskLevel;
    private String explanation;

    // Backwards-compatible constructor used internally by older code paths:
    public LoanDecision(Long applicantId, int score, boolean decision,
                        double interestRate, double maxLoanAmount, String explanation) {
        this.applicantId = applicantId;
        this.calculatedScore = score;
        this.approved = decision;
        this.interestRate = interestRate;
        this.maxLoanAmount = maxLoanAmount;
        this.explanation = explanation;
        this.riskLevel = determineRiskLevel(score);
    }

    private String determineRiskLevel(int score) {
        if (score >= 85) return "EXCELLENT";
        else if (score >= 70) return "GOOD";
        else if (score >= 60) return "FAIR";
        else if (score >= 50) return "POOR";
        return "VERY_POOR";
    }
}