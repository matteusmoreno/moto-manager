package com.matteusmoreno.moto_manager.employee.controller;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.request.UpdateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.response.EmployeeDetailsResponse;
import com.matteusmoreno.moto_manager.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Employee Controller Tests")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private CreateEmployeeRequest request;
    private Employee manager;
    private Employee mechanic;

    @BeforeEach
    void setUp() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", null, LocalDateTime.now());
        request = new CreateEmployeeRequest("user", "password", "John Doe", "john@example.com", "123.456.789-00", "(22)998554477", LocalDate.of(1990, 1, 1), EmployeeRole.ADMIN, "28994-675", "21", null);
        manager = new Employee(UUID.randomUUID(), request.username(), request.password(), request.name(), request.email(), request.phone(), request.birthDate(), Period.between(request.birthDate(), LocalDate.now()).getYears(), request.cpf(), request.role(), address, LocalDateTime.now(), null, null, true);
        mechanic = new Employee(UUID.randomUUID(), "mechanic", "password", "Jane Doe", "jane@example.com", "(22)998554477", LocalDate.of(1990, 1, 1), Period.between(request.birthDate(), LocalDate.now()).getYears(), "987.654.321-00", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should create a new employee successfully")
    void shouldCreateNewEmployeeSuccessfully() throws Exception {

        when(employeeService.createEmployee(request)).thenReturn(manager);

        String json = """
                {
                  "username": "user",
                  "password": "password",
                  "name": "John Doe",
                  "email": "john@example.com",
                  "cpf": "123.456.789-00",
                  "phone": "(22)998554477",
                  "birthDate": "1990-01-01",
                  "role": "ADMIN",
                  "zipcode": "28994-675",
                  "number": "21"
                }
                """;

        mockMvc.perform(post("/employees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(manager.getId().toString()))
                .andExpect(jsonPath("$.username").value(manager.getUsername()))
                .andExpect(jsonPath("$.name").value(manager.getName()))
                .andExpect(jsonPath("$.email").value(manager.getEmail()))
                .andExpect(jsonPath("$.phone").value(manager.getPhone()))
                .andExpect(jsonPath("$.birthDate").value(manager.getBirthDate().toString()))
                .andExpect(jsonPath("$.age").value(manager.getAge()))
                .andExpect(jsonPath("$.cpf").value(manager.getCpf()))
                .andExpect(jsonPath("$.role").value(manager.getRole().name()))
                .andExpect(jsonPath("$.active").value(manager.getActive()))
                .andExpect(jsonPath("$.address.zipcode").value(manager.getAddress().getZipcode()))
                .andExpect(jsonPath("$.address.number").value(manager.getAddress().getNumber()))
                .andExpect(jsonPath("$.address.complement").value(manager.getAddress().getComplement()))
                .andExpect(jsonPath("$.createdAt").value(manager.getCreatedAt().toString())
                );
    }

    @Test
    @DisplayName("Should find all employees successfully")
    void shouldFindAllEmployeesSuccessfully() throws Exception {
        Page<EmployeeDetailsResponse> employeePage = new PageImpl<>(Collections.checkedList(List.of(new EmployeeDetailsResponse(manager), new EmployeeDetailsResponse(mechanic)), EmployeeDetailsResponse.class));

        when(employeeService.findAllEmployees(any(Pageable.class))).thenReturn(employeePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/find-all")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validando campos de paginação
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2)) // Considerando dois funcionários no teste
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                // Validando conteúdo do primeiro funcionário (manager)
                .andExpect(jsonPath("$.content[0].id").value(manager.getId().toString()))
                .andExpect(jsonPath("$.content[0].username").value(manager.getUsername()))
                .andExpect(jsonPath("$.content[0].name").value(manager.getName()))
                .andExpect(jsonPath("$.content[0].email").value(manager.getEmail()))
                .andExpect(jsonPath("$.content[0].phone").value(manager.getPhone()))
                .andExpect(jsonPath("$.content[0].birthDate").value(manager.getBirthDate().toString()))
                .andExpect(jsonPath("$.content[0].age").value(manager.getAge()))
                .andExpect(jsonPath("$.content[0].cpf").value(manager.getCpf()))
                .andExpect(jsonPath("$.content[0].role").value(manager.getRole().name()))
                .andExpect(jsonPath("$.content[0].active").value(manager.getActive()))
                .andExpect(jsonPath("$.content[0].address.zipcode").value(manager.getAddress().getZipcode()))
                .andExpect(jsonPath("$.content[0].address.number").value(manager.getAddress().getNumber()))
                .andExpect(jsonPath("$.content[0].address.complement").value(manager.getAddress().getComplement()))
                .andExpect(jsonPath("$.content[0].createdAt").value(manager.getCreatedAt().toString()))
                // Validando conteúdo do segundo funcionário (mechanic)
                .andExpect(jsonPath("$.content[1].id").value(mechanic.getId().toString()))
                .andExpect(jsonPath("$.content[1].username").value(mechanic.getUsername()))
                .andExpect(jsonPath("$.content[1].name").value(mechanic.getName()))
                .andExpect(jsonPath("$.content[1].email").value(mechanic.getEmail()))
                .andExpect(jsonPath("$.content[1].phone").value(mechanic.getPhone()))
                .andExpect(jsonPath("$.content[1].birthDate").value(mechanic.getBirthDate().toString()))
                .andExpect(jsonPath("$.content[1].age").value(mechanic.getAge()))
                .andExpect(jsonPath("$.content[1].cpf").value(mechanic.getCpf()))
                .andExpect(jsonPath("$.content[1].role").value(mechanic.getRole().name()))
                .andExpect(jsonPath("$.content[1].active").value(mechanic.getActive()))
                .andExpect(jsonPath("$.content[1].address.zipcode").value(mechanic.getAddress().getZipcode()))
                .andExpect(jsonPath("$.content[1].address.number").value(mechanic.getAddress().getNumber()))
                .andExpect(jsonPath("$.content[1].address.complement").value(mechanic.getAddress().getComplement()))
                .andExpect(jsonPath("$.content[1].createdAt").value(mechanic.getCreatedAt().toString()));
    }

    @Test
    @DisplayName("Should update an employee successfully")
    void shouldUpdateEmployeeSuccessfully() throws Exception {
        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest(manager.getId(), "", "anotheremail@exemple.com", "", LocalDate.of(1989, 1, 1), "");
        Employee updatedManager = new Employee(manager.getId(), manager.getUsername(), manager.getPassword(), manager.getName(), updateEmployeeRequest.email(), manager.getPhone(), updateEmployeeRequest.birthDate(), Period.between(updateEmployeeRequest.birthDate(), LocalDate.now()).getYears(), manager.getCpf(), manager.getRole(), manager.getAddress(), manager.getCreatedAt(), LocalDateTime.now(), null, true);

        when(employeeService.updateEmployee(any(UpdateEmployeeRequest.class))).thenReturn(updatedManager);

        String json = """
                {
                  "id": "%s",
                  "email": "anotheremail@exemple.com",
                  "birthDate": "1989-01-01"
                  }
                """.formatted(manager.getId());

        mockMvc.perform(put("/employees/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedManager.getId().toString()))
                .andExpect(jsonPath("$.username").value(updatedManager.getUsername()))
                .andExpect(jsonPath("$.name").value(updatedManager.getName()))
                .andExpect(jsonPath("$.email").value(updatedManager.getEmail()))
                .andExpect(jsonPath("$.phone").value(updatedManager.getPhone()))
                .andExpect(jsonPath("$.birthDate").value(updatedManager.getBirthDate().toString()))
                .andExpect(jsonPath("$.age").value(updatedManager.getAge()))
                .andExpect(jsonPath("$.cpf").value(updatedManager.getCpf()))
                .andExpect(jsonPath("$.role").value(updatedManager.getRole().name()))
                .andExpect(jsonPath("$.active").value(updatedManager.getActive()))
                .andExpect(jsonPath("$.address.zipcode").value(updatedManager.getAddress().getZipcode()))
                .andExpect(jsonPath("$.address.number").value(updatedManager.getAddress().getNumber()))
                .andExpect(jsonPath("$.address.complement").value(updatedManager.getAddress().getComplement())
                );
    }

    @Test
    @DisplayName("Should disable an employee successfully")
    void shouldDisableEmployeeSuccessfully() throws Exception {
        UUID id = manager.getId();
        mockMvc.perform(delete("/employees/disable/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should enable an employee successfully")
    void shouldEnableEmployeeSuccessfully() throws Exception {
        UUID id = manager.getId();
        when(employeeService.enableEmployee(id)).thenReturn(manager);

        mockMvc.perform(patch("/employees/enable/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(manager.getId().toString()))
                .andExpect(jsonPath("$.username").value(manager.getUsername()))
                .andExpect(jsonPath("$.name").value(manager.getName()))
                .andExpect(jsonPath("$.email").value(manager.getEmail()))
                .andExpect(jsonPath("$.phone").value(manager.getPhone()))
                .andExpect(jsonPath("$.birthDate").value(manager.getBirthDate().toString()))
                .andExpect(jsonPath("$.age").value(manager.getAge()))
                .andExpect(jsonPath("$.cpf").value(manager.getCpf()))
                .andExpect(jsonPath("$.role").value(manager.getRole().name()))
                .andExpect(jsonPath("$.active").value(manager.getActive()))
                .andExpect(jsonPath("$.address.zipcode").value(manager.getAddress().getZipcode()))
                .andExpect(jsonPath("$.address.number").value(manager.getAddress().getNumber()))
                .andExpect(jsonPath("$.address.complement").value(manager.getAddress().getComplement()))
                .andExpect(jsonPath("$.createdAt").value(manager.getCreatedAt().toString()));

    }
}