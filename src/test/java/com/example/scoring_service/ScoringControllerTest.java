package com.example.scoring_service;

import com.example.applicant_service.Applicant;
import com.example.applicant_service.ApplicantService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ScoringController.class)
public class ScoringControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicantService applicantService;

    @MockitoBean
    private ScoringService scoringService;


    @Test
    public void calculateScoreTest() throws Exception {
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 480,10_000,150,24,12);
        when(applicantService.findById(1L)).thenReturn(applicant);
        when(scoringService.calculateScore(1L)).thenReturn(90);
        mockMvc.perform(get("/api/scoring/calculate/{applicantId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // checks if code is 200
                .andExpect(content().string("90")); //checks the body string = 90

    }


    @Test
    public void explanationTest() throws Exception{
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 480,10_000,150,24,12);
        when(applicantService.findById(1L)).thenReturn(applicant);
        when(scoringService.explanation(1L)).thenReturn("This is the explanation"); // mocking an explanation
        mockMvc.perform(get("/api/scoring/explanation/{applicantId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("This is the explanation"));



    }
    @Test

    public void healthTest() throws Exception{
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 480,10_000,150,24,12);
        when(applicantService.findById(1L)).thenReturn(applicant);

        mockMvc.perform(get("/api/scoring/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Scoring Service is running!"));
    }


}
