package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Employee {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("employee_name")
    private String employeeName;

    @JsonProperty("employee_salary")
    private Integer employeeSalary;

    @JsonProperty("employee_age")
    private Integer employeeAge;

    @JsonProperty("employee_title")
    private String employeeTitle;

    @JsonProperty("employee_email")
    private String employeeEmail;

    public Employee(UUID id, String employeeName, Integer employeeSalary, Integer employeeAge, String employeeTitle, String employeeEmail) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.employeeAge = employeeAge;
        this.employeeTitle = employeeTitle;
        this.employeeEmail = employeeEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id && Objects.equals(employeeName, employee.employeeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeName);
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getEmployeeSalary() {
        return employeeSalary;
    }
}
