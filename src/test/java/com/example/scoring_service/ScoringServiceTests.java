package com.example.scoring_service;

import com.example.applicant_service.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;




@ExtendWith(MockitoExtension.class)
public class ScoringServiceTests {

    @Mock
    private ApplicantService applicantService;

    @Mock
    private CreditScore creditScore;

    @InjectMocks
    private ScoringService scoringService;


    @Test
    public void calculateScoreTest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 780,10_000,150,24,12);
        when(applicantService.findById(1L)).thenReturn(applicant);
        when(creditScore.calculateScore(applicant)).thenReturn(90);

        int result = scoringService.calculateScore(1L);
        assertEquals(90,result);

    }
    @Test
    public void getExplanationTest(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 780,10_000,150,24,12);
        when(applicantService.findById(1L)).thenReturn(applicant);
        when(creditScore.getExplanation(applicant)).thenReturn("Mocked Explanation");
        String result = scoringService.explanation(1L);
        assertEquals("Mocked Explanation",result);

    }

}