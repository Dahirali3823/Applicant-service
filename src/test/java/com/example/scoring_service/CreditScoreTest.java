package com.example.scoring_service;

import com.example.applicant_service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreditScoreTest {
    CreditScore creditScore = new CreditScore();

    @Test
    public void testCalculateScore() {
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 780,10_000,150,24,12);

        double score = creditScore.calculateScore(applicant);

        assertEquals(93, score);
        assertEquals(0.8727272727272727,creditScore.scaleCreditScore(applicant.getCreditScore()));
        assertEquals(0.045,creditScore.getEstimatedRate(applicant.getCreditScore()));
        assertEquals("Credit Score Breakdown:\n" +
                "• Credit History: 87.3/100 (Weight: 40%, Raw Score: 780)\n" +
                "• Payment-to-Income: 100.0/100 (Weight: 30%, PTI Ratio: 2.2%)\n" +
                "• Debt-to-Income: 100.0/100 (Weight: 25%, DTI Ratio: 180.0%)\n" +
                "• Stability: 75.0/100 (Weight: 5%, Employment: 24 mo, Residence: 12 mo)\n" +
                "• Final Score: 93/100",creditScore.getExplanation(applicant));
        System.out.println(score);
    }

    //null applicant
    @Test
    public void nullApplicant(){
        //Applicant applicant = new Applicant();
        assertThrows(IllegalArgumentException.class, ()->creditScore.calculateScore(null));

    }
    // Negative income applicant and under 300 credit score
    @Test
    public void NegativeIncomeApplicant(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 0, 2000, 280,10_000,150,24,12);
        assertThrows(IllegalArgumentException.class, ()->creditScore.calculateScore(applicant));

    }

    // 900 credit score test
    @Test
    public void creditScoreTooHigh(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 0, 2000, 900,10_000,150,24,12);
        assertThrows(IllegalArgumentException.class, ()->creditScore.calculateScore(applicant));

    }

    @Test
    public void creditScoreBoundaryValues (){
        Applicant minScoreApplicant = new Applicant(1L, "Dave", 22, 50_000, 2000, 300, 10_000, 150, 24, 12);
        assertDoesNotThrow(() -> creditScore.calculateScore(minScoreApplicant));

        Applicant maxScoreApplicant = new Applicant(2L, "Ali", 30, 80_000, 5000, 850, 20_000, 200, 36, 24);
        assertDoesNotThrow(() -> creditScore.calculateScore(maxScoreApplicant));


    }





}
