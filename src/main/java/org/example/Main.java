package org.example;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {



    @Getter
    private static List<Employee> employees = new ArrayList<>();

    public static void main(String[] args) {
        loadEmployeesData();
        printAllEmployeesWithPensionInJSON();
        System.out.println("==================================");
        printMonthlyUpcomingEnrolleesReport();
    }

    private static void loadEmployeesData() {
        try {

            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("employee_data.json");
            assert inputStream != null;

            InputStreamReader reader = new InputStreamReader(inputStream);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(PensionPlan.class, new PensionPlanAdapter())
                    .create();

            Type empListType = new TypeToken<List<Employee>>() {}.getType();
            employees = gson.fromJson(reader, empListType);


            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void printAllEmployeesWithPensionInJSON() {

        Collections.sort(employees, (emp1, emp2) -> {
            int lastNameComparison = emp1.getLastName().compareTo(emp2.getLastName());
            if (lastNameComparison != 0) {
                return lastNameComparison;
            }
            return Double.compare(emp2.getYearlySalary(), emp1.getYearlySalary());
        });

        // Create Gson instance with adapters
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(PensionPlan.class, new PensionPlanAdapter())
                .create();

        // Print in JSON format
        System.out.println(gson.toJson(employees));
    }


    public static void printMonthlyUpcomingEnrolleesReport() {
        // Get the first and last date of the next month
        LocalDate firstDateOfNextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        LocalDate lastDateOfNextMonth = YearMonth.from(firstDateOfNextMonth).atEndOfMonth();

        List<Employee> upcomingEnrollees = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getPensionPlan() == null && isQualifyForEnrollment(employee, firstDateOfNextMonth, lastDateOfNextMonth)) {
                upcomingEnrollees.add(employee);
            }
        }

         Collections.sort(upcomingEnrollees, Comparator.comparing(Employee::getEmploymentDate));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        System.out.println(gson.toJson(upcomingEnrollees));
    }

    private static boolean isQualifyForEnrollment(Employee employee, LocalDate startDate, LocalDate endDate) {
        return employee.getEmploymentDate().plusYears(5).isAfter(startDate.minusDays(1)) && // On or after the employment date plus 5 years
                employee.getEmploymentDate().isBefore(endDate.plusDays(1)); // Before or on the end date
    }

}