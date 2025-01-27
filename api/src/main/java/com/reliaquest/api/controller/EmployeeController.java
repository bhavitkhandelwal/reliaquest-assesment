package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmplyeeOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v1/employee")
public class EmployeeController implements IEmployeeController{

    @Autowired
    private EmplyeeOperationsService emplyeeOperationsService;

    @Override
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Employee> createEmployee(@RequestBody Map employeeInput) {
        Employee employee = emplyeeOperationsService.createEmployee(employeeInput);
        return ResponseEntity.ok(employee);
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> allEmployees = emplyeeOperationsService.getAllEmployees();
        return ResponseEntity.ok(allEmployees);
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        List<Employee> searchedEmp = emplyeeOperationsService.getEmployeesByNameSearch(searchString);
        return ResponseEntity.ok(searchedEmp);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee = emplyeeOperationsService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(emplyeeOperationsService.getHighestSalaryOfEmployees());
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> top10Emp = emplyeeOperationsService.getTopTenHighestEarningEmployeeNames();
        return ResponseEntity.ok(top10Emp);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(emplyeeOperationsService.deleteEmployeeById(id));
    }
}
