package com.example.loan_service;

import com.example.applicant_service.Applicant;
import com.example.scoring_service.CreditScore;
import org.springframework.stereotype.Service;

@Service
public class LoanDecisionService {
    private final CreditScore creditScore;

    public LoanDecisionService(CreditScore creditScore) {
        this.creditScore = creditScore;
    }

    // risk-based interest rate using PD*LGD + cost-of-funds + ops
    public double getInterestRate(int actualCreditScore, double loanAmount) {
        double baseRate = 0.025; // cost of funds 2.5%
        double riskPremium = calculateRiskPremium(actualCreditScore);
        double operationalCost = Math.min(0.02, 5000.0 / Math.max(1.0, loanAmount)); // scale ops cost
        return Math.round((baseRate + riskPremium + operationalCost)*100)/100.0;
    }

    private double calculateRiskPremium(int creditScore) {
        double pd = calculateProbabilityOfDefault(creditScore);
        double lgd = 0.45;
        return pd * lgd * 1.5; // margin factor
    }

    private double calculateProbabilityOfDefault(int creditScore) {
        if (creditScore >= 750) return 0.015;
        if (creditScore >= 700) return 0.025;
        if (creditScore >= 650) return 0.045;
        if (creditScore >= 600) return 0.075;
        if (creditScore >= 550) return 0.125;
        return 0.200;
    }

    // Max loan based on PTI capacity: new loan payment <= (monthlyIncome*PTIcap - existingDebtPayments)
    private double getMaxLoanAmount(Applicant applicant) {
        double monthlyIncome = (applicant.getIncome()/12);
        double existingDebtPayments = applicant.getMonthlyDebtPayments();
        double ptiCap = 0.28; // new loan PTI cap
        double maxPayment = monthlyIncome * ptiCap - existingDebtPayments;
        if (maxPayment <= 0) return 0;
        double estimatedRate = getEstimatedRate(applicant.getCreditScore()) / 12.0;
        int termMonths = 60;
        if (estimatedRate == 0) return maxPayment * termMonths;
        double maxLoan = maxPayment * (Math.pow(1 + estimatedRate, termMonths) - 1)
                / (estimatedRate * Math.pow(1 + estimatedRate, termMonths));
        return Math.round(Math.min(maxLoan, 1_000_000)*100.0)/100.0;
    }

    private double getEstimatedRate(int creditScore) {
        if (creditScore >= 750) return 0.045;
        else if (creditScore >= 700) return 0.065;
        else if (creditScore >= 650) return 0.085;
        else if (creditScore >= 600) return 0.115;
        return 0.155;
    }

    // approval checks using actual monthly payments and stability
    public boolean determineApproval(Applicant applicant, double maxLoanAmount) {
        if (applicant.getCreditScore() < 580) return false; // floor
        if (applicant.getLoanAmount() > maxLoanAmount) return false;

        double monthlyLoanPayment = calculateMonthlyLoanPayment(applicant);
        double totalMonthlyDebt = applicant.getMonthlyDebtPayments() + monthlyLoanPayment;
        double totalDTI = totalMonthlyDebt / (applicant.getIncome()/12);
        if (totalDTI > 0.43) return false;
        if (applicant.getMonthsEmployed() < 6) return false;

        return true;
    }

    private double calculateMonthlyLoanPayment(Applicant applicant) {
        double loanAmount = applicant.getLoanAmount();
        double annualRate = getEstimatedRate(applicant.getCreditScore());
        double monthlyRate = annualRate / 12.0;
        int termMonths = 60;
        if (monthlyRate == 0) return loanAmount / termMonths;
        double payment = loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, termMonths))
                / (Math.pow(1 + monthlyRate, termMonths) - 1);
        return Math.round(payment*100)/100.0;
    }

    public LoanDecision evaluateLoan(Applicant applicant) {
        int score = creditScore.calculateScore(applicant);
        String explanation = creditScore.getExplanation(applicant);
        double maxLoanAmount = getMaxLoanAmount(applicant);
        double interestRate = getInterestRate(applicant.getCreditScore(), applicant.getLoanAmount());
        boolean decision = determineApproval(applicant, maxLoanAmount);
        double monthlyPayment = calculateMonthlyLoanPayment(applicant);
        double totalMonthlyDebt = applicant.getMonthlyDebtPayments() + monthlyPayment;
        double totalDTI = monthlyPayment > 0 ? totalMonthlyDebt / (applicant.getIncome()/12) : 0.0;

        return new LoanDecision(
                applicant.getId(),
                score,
                applicant.getCreditScore(),
                decision,
                interestRate,
                maxLoanAmount,
                applicant.getLoanAmount(),
                monthlyPayment,
                totalDTI,
                decision ? determineRiskLevel(score) : "REJECTED",
                explanation
        );
    }

    private String determineRiskLevel(int score) {
        if (score >= 85) return "EXCELLENT";
        else if (score >= 70) return "GOOD";
        else if (score >= 60) return "FAIR";
        else if (score >= 50) return "POOR";
        return "VERY_POOR";
    }
}
