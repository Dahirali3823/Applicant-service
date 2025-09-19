package com.example.applicant_service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicantController.class)
@Import(ControllerTests.MockServiceConfig.class)
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicantService service;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public ApplicantService applicantService() {
            return Mockito.mock(ApplicantService.class);
        }
    }
    // posting an applicant
    @Test
    void createApplicant() throws Exception {
        Applicant mockApplicant = new Applicant(1L, "Dave", 22, 22000, 0, 680, 10000, 18, 0, 0);
        when(service.save(any(Applicant.class))).thenReturn(mockApplicant);

        mockMvc.perform(post("/api/applicants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id": 1,
                        "name": "Dave",
                        "age": 22,
                        "income": 22000,
                        "debt": 0,
                        "creditScore": 680,
                        "loanAmount": 10000,
                        "monthsEmployed": 18,
                        "residentMonths": 0,
                        "monthlyDebtPayments": 0
                      }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dave"))
                .andExpect(jsonPath("$.age").value(22))
                .andExpect(jsonPath("$.income").value(22000))
                .andExpect(jsonPath("$.debt").value(0))
                .andExpect(jsonPath("$.creditScore").value(680));
    }
    //putting an applicant
    @Test
    void putApplicant() throws Exception {
        when(service.save(any(Applicant.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        mockMvc.perform(put("/api/applicants/id/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id": 1,
                        "name": "Jack",
                        "age": 19,
                        "income": 20000,
                        "debt": 1000,
                        "creditScore": 690,
                        "loanAmount": 10000,
                        "monthsEmployed": 18,
                        "residentMonths": 0,
                        "monthlyDebtPayments": 0
                      }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jack"))
                .andExpect(jsonPath("$.age").value(19))
                .andExpect(jsonPath("$.income").value(20000.0))
                .andExpect(jsonPath("$.debt").value(1000))
                .andExpect(jsonPath("$.creditScore").value(690));
    }

    // getting all applicants
    @Test
    void getApplicant() throws Exception {
        List<Applicant> applicants = List.of(
        new Applicant(1L, "Dave", 23, 25000.0, 0.0, 650,50000,18,0,0),
        new Applicant(2L, "Jack", 33, 250000.0, 40000.0, 750,5000.0,150,12,12));
        when(service.findAll()).thenReturn(applicants);
        mockMvc.perform(get("/api/applicants").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Dave"))
                .andExpect(jsonPath("$[1].name").value("Jack"));


    }
    // getting applicant by id
    @Test
    void getApplicantID() throws Exception {
        Applicant mockApplicant = new Applicant(1L, "Dave", 23, 25000.0, 0.0, 650,50000,18,0,0);
        when(service.findById(1L)).thenReturn(mockApplicant);

        mockMvc.perform(get("/api/applicants/id/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dave"))
                .andExpect(jsonPath("$.age").value(23))
                .andExpect(jsonPath("$.income").value(25000.0))
                .andExpect(jsonPath("$.debt").value(0.0))
                .andExpect(jsonPath("$.creditScore").value(650))
        .andExpect(jsonPath("$.loanAmount").value(50000.0));

    }
    // getting applicant by name
    @Test
    void getApplicantName() throws Exception {
        Applicant mockApplicant = new Applicant(1L, "Dave", 23, 25000.0, 0.0, 650,50000,18,0,0);
        when(service.findByName("Dave")).thenReturn(mockApplicant);
        mockMvc.perform(get("/api/applicants/name/{name}","Dave")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dave"));

    }
    // applicant not found
    @Test
    void applicantNotFound() throws Exception {
        when(service.findById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/applicants/id/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    // delete all applicants
    @Test
    void deleteApplicant() throws Exception {
        mockMvc.perform(delete("/api/applicants/delete-all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
    // delete applicant by id
    @Test
    void deleteApplicantbyID() throws Exception {
        Applicant mockApplicant = new Applicant(1L, "Dave", 23, 25000.0, 0.0, 650,50000,18,0,0);
        when(service.findById(1L)).thenReturn(mockApplicant);
        mockMvc.perform(delete("/api/applicants/id/{id}",1L)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
    // applicant to be deleted not found
    @Test
    void deleteApplicantNotFound() throws Exception {
        when(service.findById(1L)).thenReturn(null);
        mockMvc.perform(delete("/api/applicants/id/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    // updating parts of applicant
    @Test
    void updateApplicant() throws Exception {
        Applicant applicant = new Applicant(1L, "Dave", 22, 100_000, 2000, 780,10_000,150,24,12);
        when(service.findById(1L)).thenReturn(applicant);
        when(service.save(any(Applicant.class))).thenAnswer(i -> i.getArguments()[0]);
        String part = "{\"name\":\"Ali\",\"age\":44,\"income\":2000.0}";
        mockMvc.perform(patch("/api/applicants/id/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(part))
                .andExpect(jsonPath("$.name").value("Ali"))
                .andExpect(jsonPath("$.age").value(44))
                .andExpect(jsonPath("$.income").value(2000.0));
        System.out.println(applicant);
    }
}


