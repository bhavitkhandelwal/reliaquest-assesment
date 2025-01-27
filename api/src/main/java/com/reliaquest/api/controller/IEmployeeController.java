package com.reliaquest.api.controller;

import java.util.List;
import java.util.Map;

import com.reliaquest.api.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface IEmployeeController<Entity, Input> {

    ResponseEntity<List<Employee>> getAllEmployees();

    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput);

    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);
}
