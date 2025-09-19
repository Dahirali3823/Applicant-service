package com.example.applicant_service;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "applicants")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 18,message = "Age has to be 18 or older")
    @Max(value = 100, message = "Age has to be younger than 100")
    private int age;

    @DecimalMin(value = "0.0",message = "Income must be Positive")
    private double income;

    @DecimalMin(value = "0.0",message = "Debt must be Positive")
    private double debt;

    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score cannot exceed 850")
    private int creditScore;

    @DecimalMin(value = "0.0",message = "Loan Amount must be Positive")
    private double loanAmount;

    @DecimalMin(value = "0.0",message = "Loan Amount must be Positive")
    private double MonthlyDebtPayments;
    @DecimalMin(value = "0.0",message = "Loan Amount must be Positive")
    private int monthsEmployed;
    @DecimalMin(value = "0.0",message = "Loan Amount must be Positive")
    private int ResidentMonths;



}
