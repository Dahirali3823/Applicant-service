package com.example.loan_service;

import com.example.applicant_service.Applicant;

import com.example.scoring_service.CreditScore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)


public class LoanDecisionServiceTests{



    @InjectMocks
    LoanDecisionService loanDecisionService;

    @Mock
    CreditScore creditScore;

    //good applicant
    @Test
    public void evaluateLoanTest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 780,10_000,150,24,12);
        when(creditScore.calculateScore(applicant)).thenReturn(90);
        LoanDecision loanDecision = loanDecisionService.evaluateLoan(applicant);
        assertEquals(90,loanDecision.getCalculatedScore());
        assertTrue(loanDecision.isApproved());
        assertTrue(loanDecision.getMaxLoanAmount()>0);
        assertTrue(loanDecision.getTotalDTI()<0.43);
        assertEquals("EXCELLENT",loanDecision.getRiskLevel());



    }
    // applicant with low credit score
    @Test
    public void evaluateBadCreditTest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 480,10_000,150,24,12);
        when(creditScore.calculateScore(applicant)).thenReturn(40);
        LoanDecision loanDecision = loanDecisionService.evaluateLoan(applicant);
        assertFalse(loanDecision.isApproved());
        assertEquals("REJECTED",loanDecision.getRiskLevel());


    }
    @Test
    // applicant with a too high loan request
    public void highLoanRequestTest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 50_000, 2000, 680,100_000,150,24,12);
        when(creditScore.calculateScore(applicant)).thenReturn(60);
        LoanDecision loanDecision = loanDecisionService.evaluateLoan(applicant);
        assertFalse(loanDecision.isApproved());
        assertTrue(loanDecision.getRequestedAmount()>loanDecision.getMaxLoanAmount());
        System.out.println(loanDecision.getMaxLoanAmount());


    }
    @Test
    // applicant with a high DTI ratio
    public void highDTITest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 10_000, 100_000, 680,10_000,500,24,12);
        when(creditScore.calculateScore(applicant)).thenReturn(60);
        LoanDecision loanDecision = loanDecisionService.evaluateLoan(applicant);
        assertFalse(loanDecision.isApproved());
        // higher than 43% dti is auto reject
        assertTrue(loanDecision.getTotalDTI()>0.43);
        System.out.println(loanDecision.getTotalDTI());

    }
    @Test
    //applicant with unstable job <6 months
    public void shortEmploymentTest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 50_000, 2000, 780,10_000,150,2,12);
        when(creditScore.calculateScore(applicant)).thenReturn(68);
        LoanDecision loanDecision = loanDecisionService.evaluateLoan(applicant);
        assertFalse(loanDecision.isApproved());
        assertEquals(2,applicant.getMonthsEmployed());
    }
    @Test
    //ensuring applicant risk level is correct
    public void riskLevelTest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 50_000, 2000, 780,10_000,150,8,12);
        when(creditScore.calculateScore(applicant)).thenReturn(68);
        LoanDecision loanDecision = loanDecisionService.evaluateLoan(applicant);
        assertEquals("FAIR",loanDecision.getRiskLevel());
        System.out.println(loanDecision.getRiskLevel());
        assertEquals(68,loanDecision.getCalculatedScore());
        System.out.println(loanDecision.getCalculatedScore());

    }
    



}