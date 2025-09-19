package com.example.loan_service;



import com.example.applicant_service.Applicant;
import com.example.applicant_service.ApplicantService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)

public class LoanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanDecisionService loanDecisionService;
    @Autowired
    private ApplicantService applicantService;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public ApplicantService applicantService() {
            return Mockito.mock(ApplicantService.class);
        }
    }

    @Test
    void evaluateLoan() throws Exception {
        //mock applicant
        Applicant applicant = new Applicant();
        applicant.setId(1L);
        when(applicantService.findById(1L)).thenReturn(applicant);

        //mock loan
        LoanDecision loan = new LoanDecision(3L,90,780,true,0.06,50063.42,
                15000,279.65,0.08,"EXCELLENT","Explanation");
        when(loanDecisionService.evaluateLoan(applicant)).thenReturn(loan);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/loans/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicantId").value(3))
                .andExpect(jsonPath("$.calculatedScore").value(90))
                .andExpect(jsonPath("$.actualCreditScore").value(780))
                .andExpect(jsonPath("$.approved").value(true))
                .andExpect(jsonPath("$.interestRate").value(0.06));


    }
}
