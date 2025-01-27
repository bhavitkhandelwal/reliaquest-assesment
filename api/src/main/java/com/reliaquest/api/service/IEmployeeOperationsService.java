package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;

import java.util.List;
import java.util.Map;

public interface IEmployeeOperationsService {
    List<Employee> getAllEmployees();

    List<Employee> getEmployeesByNameSearch(String searchString);

    Employee getEmployeeById(String id);

    Integer getHighestSalaryOfEmployees();

    List<String> getTopTenHighestEarningEmployeeNames();

    Employee createEmployee(Map<String, Object> employeeInput);

    String deleteEmployeeById(String id);
}
