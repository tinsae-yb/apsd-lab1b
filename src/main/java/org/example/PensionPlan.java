package org.example;



import lombok.Data;

import java.time.LocalDate;

@Data
public class PensionPlan {
    private String planReferenceNumber;
    private LocalDate enrollmentDate;
    private double monthlyContribution;
}
