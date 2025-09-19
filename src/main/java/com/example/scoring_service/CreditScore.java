package com.example.scoring_service;

import com.example.applicant_service.Applicant;
import org.springframework.stereotype.Component;

@Component
public class CreditScore {

    public int calculateScore(Applicant applicant) {
        validateInput(applicant);

        // 40% weight for Credit History
        double creditHistory = scaleCreditScore(applicant.getCreditScore()) *40;

        //30% weight Payment To Income ratio
        double ptiRatio = paymentToIncomeRatio(applicant);
        double ptiFactor = PtiRatio(ptiRatio)*30;

        //25% weight to Debt to Income Ratio using Monthly debt payments
        double dtiRatio = debtToIncomeRatio(applicant)*25;

        double stabilityScore =  scoreStability(applicant)*5;

        double score = creditHistory + ptiFactor + dtiRatio + stabilityScore;
        System.out.println(
                "Credit History: " + creditHistory +
                        ", PTI Factor: " + ptiFactor +
                        ", DTI Ratio: " + dtiRatio +
                        ", Stability Score: " + stabilityScore
        );


        return (int) Math.min(score,100);


    }
    private void validateInput(Applicant applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }
        if (applicant.getIncome() <= 0) {
            throw new IllegalArgumentException("Income must be positive");
        }
        if (applicant.getCreditScore() < 300 || applicant.getCreditScore() > 850) {
            throw new IllegalArgumentException("Credit score must be between 300-850");
        }
    }


    // scales credit score to a 0-1 range
    public double scaleCreditScore(int score){
        return(score - 300.0) / 550.0;
    }


    // returns a debt to income ratio
    private double debtToIncomeRatio(Applicant applicant) {
        double ratio = applicant.getMonthlyDebtPayments()/ (applicant.getIncome()/12.0);
        if(ratio<= 0.15){
            return 1.0;
        }
        else if(ratio<= 0.25){
            return 0.75;
        }
        else if(ratio<= 0.35){
            return 0.5;
        }
        else if(ratio<= 0.43){
            return 0.25;
        }
        else{
            return 0;
        }
    }
    //calculates ratio for payment to income using formulas
    private double paymentToIncomeRatio(Applicant applicant) {
        double loanAmount = applicant.getLoanAmount();
        double estimatedRate = getEstimatedRate(applicant.getCreditScore()) / 12.0; //Monthly rate
        int termMonths = 60;
        //amortized monthly payment formula
        double monthlyPayment = loanAmount * (estimatedRate * Math.pow(1 + estimatedRate, termMonths))
                / (Math.pow(1 + estimatedRate, termMonths) - 1);
        return monthlyPayment / (applicant.getIncome() / 12);
    }

    // returns a payment to income score according to the ratio
    private double PtiRatio(double ratio){
        if (ratio <= 0.20) return 1.0;
        else if (ratio <= 0.28) return 0.75;
        else if (ratio <= 0.35) return 0.5;
        else if (ratio <= 0.50) return 0.25;
        else return 0;

    }
    // returns an estimated interest rate according to creditscore
    public double getEstimatedRate(int creditScore) {
        if (creditScore >= 750) return 0.045; // 4.5%
        if (creditScore >= 700) return 0.065; // 6.5%
        if (creditScore >= 650) return 0.085; // 8.5%
        if (creditScore >= 600) return 0.115; // 11.5%
        else return 0.155; // 15.5%
    }

    // returns averaged score of residence and employment stability using a normalized scale
    private double scoreStability(Applicant applicant) {
        double residenceScore =Math.min(applicant.getResidentMonths()/24.0,1);
        double EmploymentScore =Math.min(applicant.getMonthsEmployed()/24.0,1);

        return (residenceScore + EmploymentScore)/2;

    }
    public String getExplanation(Applicant applicant) {
        double creditFactor = scaleCreditScore(applicant.getCreditScore())*100;
        double ptiRatio = paymentToIncomeRatio(applicant);
        double ptiFactor = PtiRatio(ptiRatio)*100;
        double dtiRatio = (applicant.getMonthlyDebtPayments() / (applicant.getIncome() / 12.0))*100;
        double dtiFactor = debtToIncomeRatio(applicant)*100;
        double stabilityFactor = scoreStability(applicant)*100;

        return "Credit Score Breakdown:\n" +
                String.format("• Credit History: %.1f/100 (Weight: 40%%, Raw Score: %d)\n",
                        creditFactor, applicant.getCreditScore()) +
                String.format("• Payment-to-Income: %.1f/100 (Weight: 30%%, PTI Ratio: %.1f%%)\n",
                        ptiFactor, ptiRatio * 100) +
                String.format("• Debt-to-Income: %.1f/100 (Weight: 25%%, DTI Ratio: %.1f%%)\n",
                        dtiFactor, dtiRatio * 100) +
                String.format("• Stability: %.1f/100 (Weight: 5%%, Employment: %d mo, Residence: %d mo)\n",
                        stabilityFactor, applicant.getMonthsEmployed(), applicant.getResidentMonths()) +
                String.format("• Final Score: %d/100", calculateScore(applicant));
    }




}
