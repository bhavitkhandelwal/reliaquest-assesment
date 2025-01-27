package com.reliaquest.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.constants.Constants;
import com.reliaquest.api.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmplyeeOperationsService implements IEmployeeOperationsService{

    @Autowired
    private IMockEmployeeApiService apiService;

    @Autowired
    private ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(EmplyeeOperationsService.class);

    @Override
    public List<Employee> getAllEmployees() {
        ResponseEntity<JsonNode> responseEntity = apiService.get(Constants.EMPLOYEES);
        logger.info("Endpoint: " + Constants.EMPLOYEES);
        return processResponse(responseEntity, new TypeReference<>() {});
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        return getAllEmployees().stream()
                .filter(emp -> emp.getEmployeeName() != null && emp.getEmployeeName().contains(searchString))
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeById(String id) {
        ResponseEntity<JsonNode> responseEntity = apiService.get(Constants.EMPLOYEES + "/" + id);
        return processResponse(responseEntity, new TypeReference<>() {});
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        int highest = Integer.MIN_VALUE;
        for (Employee emp : getAllEmployees()) {
            if (emp.getEmployeeSalary() > highest) {
                highest = emp.getEmployeeSalary();
            }
        }
        return highest;
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<String> sortedEmpNames = getAllEmployees()
                .stream().sorted((o1, o2) -> o2.getEmployeeSalary() - o1.getEmployeeSalary())
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList());
        return sortedEmpNames.subList(0, Math.min(sortedEmpNames.size(), 10));
    }

    @Override
    public Employee createEmployee(Map<String, Object> employeeInput) {
        ResponseEntity<JsonNode> responseEntity = apiService.post(Constants.EMPLOYEES, employeeInput);
        return processResponse(responseEntity, new TypeReference<>() {});
    }

    @Override
    public String deleteEmployeeById(String id) {
        Employee employee = getEmployeeById(id);
        apiService.delete(Constants.EMPLOYEES, employee.getEmployeeName());
        return employee.getEmployeeName();
    }

    private <T> T processResponse(ResponseEntity<JsonNode> responseEntity, TypeReference<T> type) {
        Response response = mapper.convertValue(responseEntity.getBody(), Response.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            logger.info("API execution was successful, Http Code: {}", responseEntity.getStatusCode());
            if (Constants.SUCCESS.equalsIgnoreCase(response.getStatus())) {
                return mapper.convertValue(response.getData(), type);
            } else {
                logger.error("API execution was not successful, status: {}", response.getStatus());
                throw new RuntimeException("Internal Server Error");
            }
        } else {
            logger.error("API execution was failed with Http Code: {}", responseEntity.getStatusCode());
            byte[] bodyBytes = (responseEntity.hasBody()) ? Objects.requireNonNull(responseEntity.getBody()).toString().getBytes() : null;
            if (responseEntity.getStatusCode().is4xxClientError()) {
                throw HttpClientErrorException.create(responseEntity.getStatusCode(), "Client Error", responseEntity.getHeaders(),
                        bodyBytes, StandardCharsets.UTF_8);
            }
            if (responseEntity.getStatusCode().is5xxServerError()) {
                throw HttpServerErrorException.create(responseEntity.getStatusCode(), "Server Side Error", responseEntity.getHeaders(),
                        bodyBytes, StandardCharsets.UTF_8);
            }
            throw new RestClientException("");
        }
    }

}
