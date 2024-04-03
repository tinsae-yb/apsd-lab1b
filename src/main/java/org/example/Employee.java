package org.example;


    import lombok.Data;

    import java.time.LocalDate;

    @Data
    public class Employee implements Comparable<Employee> {
        private Long employeeId;
        private String firstName;
        private String lastName;
        private LocalDate employmentDate;
        private Double yearlySalary;
        private PensionPlan pensionPlan;



        @Override
        public int compareTo(Employee other) {

            int lastNameCompare = this.lastName.compareTo(other.lastName);
            if (lastNameCompare != 0) {
                return lastNameCompare;
            } else {
                return Double.compare(other.yearlySalary, this.yearlySalary);
            }
        }


    }


