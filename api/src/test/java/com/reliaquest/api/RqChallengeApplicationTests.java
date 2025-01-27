package com.reliaquest.api;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.constants.Constants;
import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.model.DeleteMockEmployeeInput;
import com.reliaquest.api.model.DeletedEmployeeResponse;
import com.reliaquest.api.model.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@AutoConfigureMockMvc
class RqChallengeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    RestTemplate restTemplate;

    @InjectMocks
    private EmployeeController controller;

    @Value("${api.base.url}")
    private String apiBaseUrl;

    private static JsonNode allEmployeeJson;
    private static JsonNode singleEmployeeJson;
    private static List<String> empNames;

    @BeforeAll
    public static void init() {
        allEmployeeJson = TestUtils.readJson("all-emp-data.json");
        assertNotNull(allEmployeeJson);
        singleEmployeeJson = TestUtils.readJson("single-emp-data.json");
        assertNotNull(singleEmployeeJson);
        empNames = Arrays.asList("Lloyd Tremblay","Jonna Swaniawski","Ivory Hegmann","Shawanna Parker III","Mrs. Wallace Cassin","Palmer Zieme II","Miss Jeffie Schuster","Quinn Kiehn PhD","Gisela Weimann","Clay Schaefer");
    }

    @Test
    void getAllEmployees_withValidData_returnsListOf50Employees() throws Exception {
        mockGetAllSuccess();
        mockMvc.perform(get("/v1/employee"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    List<Employee> list = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(50, list.size());
                });
    }

    @Test
    void getEmployeesByNameSearch_WithValidName_returnsDataForEmployeeWhoseNameMatches() throws Exception {
        mockGetAllSuccess();
        mockMvc.perform(get("/v1/employee/search/Garth Maggio"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    List<Employee> list = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(1, list.size());
                });
    }

    @Test
    void getEmployeeById_withValidId_returnsDataForEmployee() throws Exception {
        mockSingleEmployee();
        mockMvc.perform(get("/v1/employee/e8093f28-c029-4e92-92c7-de5b99f4c3f1"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    Employee emp = mapper.readValue(result.getResponse().getContentAsString(), Employee.class);
                    assertEquals(emp.getEmployeeName(), "Tiger Nixon");
                    assertEquals(emp.getEmployeeSalary(), 320800);
                });
    }

    @Test
    void getHighestSalaryOfEmployees_withValidData_returnsHighestSalary() throws Exception {
        mockGetAllSuccess();
        mockMvc.perform(get("/v1/employee/highestSalary"))
                .andExpect(status().isOk())
                .andDo(result -> assertEquals("480255", result.getResponse().getContentAsString()));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_withValidData_returnsTopTenHighestSalaryEmployeeNames() throws Exception {
        mockGetAllSuccess();
        mockMvc.perform(get("/v1/employee/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    List<String> list = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
                    assertEquals(10, list.size());
                    assertIterableEquals(empNames, list);
                });
    }

    @Test
    void createEmployee_withValidDataOfEmployee_AddEmployeeInEmployeeDataBase() throws Exception {
        Map<String, Object> inputMap = new LinkedHashMap<>();
        inputMap.put("name", "Tiger Nixon");
        inputMap.put("salary", 320800);
        inputMap.put("age", 61);
        String requestBody = mapper.writeValueAsString(inputMap);
        when(restTemplate.postForEntity(apiBaseUrl + Constants.EMPLOYEES, inputMap, JsonNode.class))
                .thenReturn(ResponseEntity.ok(singleEmployeeJson));
        mockMvc.perform(post("/v1/employee")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    Employee emp = mapper.readValue(result.getResponse().getContentAsString(), Employee.class);
                    assertEquals(emp.getEmployeeName(), "Tiger Nixon");
                    assertEquals(emp.getEmployeeSalary(), 320800);
                });
    }

    @Test
    void deleteEmployeeById_withValidId_removesEmployeeFromList() throws Exception {
        mockSingleEmployee();
        DeleteMockEmployeeInput deleteMockEmployeeInput = new DeleteMockEmployeeInput("Tiger Nixon");
        deleteMockEmployeeInput.setName("Tiger Nixon");
        HttpEntity<DeleteMockEmployeeInput> requestEntity = new HttpEntity<>(deleteMockEmployeeInput);

        DeletedEmployeeResponse<Boolean> mockResponse = new DeletedEmployeeResponse<>(null, DeletedEmployeeResponse.Status.HANDLED, null);
        ResponseEntity<DeletedEmployeeResponse<Boolean>> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        when(restTemplate.exchange(apiBaseUrl + Constants.EMPLOYEES, HttpMethod.DELETE, requestEntity, new ParameterizedTypeReference<DeletedEmployeeResponse<Boolean>>() {})).thenReturn(responseEntity);

        mockMvc.perform(delete("/v1/employee/e8093f28-c029-4e92-92c7-de5b99f4c3f1"))
                .andExpect(status().isOk())
                .andDo(result -> assertEquals("Tiger Nixon", result.getResponse().getContentAsString()));
    }

    private void mockGetAllSuccess() {
        when(restTemplate.getForEntity(apiBaseUrl + Constants.EMPLOYEES, JsonNode.class))
                .thenReturn(ResponseEntity.ok(allEmployeeJson));
    }

    private void mockSingleEmployee() {
        when(restTemplate.getForEntity(apiBaseUrl + Constants.EMPLOYEES + "/e8093f28-c029-4e92-92c7-de5b99f4c3f1", JsonNode.class))
                .thenReturn(ResponseEntity.ok(singleEmployeeJson));
    }
}