package com.example.applicant_service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ApplicantServiceTests {
    @Mock
    ApplicantRepository applicantRepository;

    @InjectMocks
    ApplicantService applicantService;

    @Test
    void ApplicantSave(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 22000, 0, 680,10000,18,0,0);
        when(applicantRepository.save(any(Applicant.class))).thenReturn(applicant);
        Applicant savedApplicant = applicantService.save(applicant);
        assert savedApplicant.getId()== 1L;
        assert savedApplicant.getName().equals("Dave");
        assert savedApplicant.getAge() == 22;
        verify(applicantRepository).save(applicant);


    }
    @Test
    void findAllApplicants(){
        List<Applicant> applicants = List.of(
                new Applicant(1L, "Dave", 23, 25000.0, 0.0, 650,50000,18,0,0),
                new Applicant(1L, "Jack", 22, 22000, 0, 680,10000,18,0,0));
        when(applicantRepository.findAll()).thenReturn(applicants);
        List<Applicant> result = applicantService.findAll();
        assertEquals(2, result.size());
        assertEquals(1L,result.getFirst().getId());
        assertEquals("Dave",result.getFirst().getName());
        verify(applicantRepository).findAll();

    }
    @Test
    void findApplicantById(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 22000, 0, 680,10000,18,0,0);
        when(applicantRepository.findById(1L)).thenReturn(Optional.of(applicant));
        Applicant result = applicantService.findById(1L);
        assertEquals(1L,result.getId());
        verify(applicantRepository).findById(1L);
    }
    @Test
    void findNullApplicantById(){
        when(applicantRepository.findById(1L)).thenReturn(Optional.empty());
        Applicant result = applicantService.findById(1L);
        assertNull(result);
        verify(applicantRepository).findById(1L);
    }
    @Test
    void findApplicantByName(){
        Applicant applicant = new Applicant(1L, "Dave", 22, 22000, 0, 680,10000,18,0,0);
        when(applicantRepository.findByName("Dave")).thenReturn(Optional.of(applicant));
        Applicant result = applicantService.findByName("Dave");
        assertEquals("Dave",result.getName());
        verify(applicantRepository).findByName("Dave");
    }
    @Test
    void findNullApplicantByName(){
        when(applicantRepository.findByName("John")).thenReturn(Optional.empty());
        Applicant result = applicantService.findByName("John");
        assertNull(result);
        verify(applicantRepository).findByName("John");
    }
    @Test
    void deleteApplicant(){
        applicantService.deleteAll();
        verify(applicantRepository).deleteAll();
    }
    @Test
    void deleteApplicantById(){
        applicantService.delete(1L);
        verify (applicantRepository).deleteById(1L);
    }







}
