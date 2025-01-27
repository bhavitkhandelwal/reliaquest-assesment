package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.reliaquest.api.model.DeletedEmployeeResponse;
import com.reliaquest.api.model.DeleteMockEmployeeInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MockEmployeeApiService implements IMockEmployeeApiService{

    @Autowired
    private RestTemplate restTemplate;

    // Inject property from application.properties
    @Value("${api.base.url}")
    private String apiBaseUrl;

    private static final Logger logger = LoggerFactory.getLogger(MockEmployeeApiService.class);

    @Override
    public ResponseEntity<JsonNode> get(String endpoint) {
        logger.info("GET Api call for: {}{}", apiBaseUrl, endpoint);
        return restTemplate.getForEntity(apiBaseUrl + endpoint, JsonNode.class);
    }

    @Override
    public ResponseEntity<JsonNode> post(String endpoint, Map<String, Object> request) {
        logger.info("POST Api call for: {}{}", apiBaseUrl, endpoint);
        return restTemplate.postForEntity(apiBaseUrl + endpoint, request, JsonNode.class);
    }

    @Override
    public void delete(String endpoint, String employeeName) {
        logger.info("DELETE Employee Api call for: {}{}", apiBaseUrl, endpoint);
        DeleteMockEmployeeInput deleteMockEmployeeInput = new DeleteMockEmployeeInput(employeeName);
        deleteMockEmployeeInput.setName(employeeName);
        HttpEntity<DeleteMockEmployeeInput> requestEntity = new HttpEntity<>(deleteMockEmployeeInput);
        restTemplate.exchange(
                apiBaseUrl + endpoint,
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<DeletedEmployeeResponse<Boolean>>() {}
        );
    }
}
