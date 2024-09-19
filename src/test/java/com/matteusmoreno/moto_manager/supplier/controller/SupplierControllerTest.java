package com.matteusmoreno.moto_manager.supplier.controller;

import com.matteusmoreno.moto_manager.config.SecurityConfiguration;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.response.SupplierListResponse;
import com.matteusmoreno.moto_manager.supplier.service.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SupplierController.class)
@Import(SecurityConfiguration.class)
@DisplayName("Supplier Controller Tests")
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    private Supplier supplier;
    private Supplier anotherSupplier;
    private Jwt adminJwt;

    @BeforeEach
    void setUp() {
        adminJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "ADMIN")
                .build();
        supplier = new Supplier(1L, "SUPPLIER NAME", "00.000.000/0000-00", "(00)000000000", "supplier@email.com", LocalDateTime.now(), null, null, true);
        anotherSupplier = new Supplier(2L, "ANOTHER SUPPLIER NAME", "11.111.111/1111-11", "(11)111111111", "anothersupplier@email.com", LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should create a new supplier successfully")
    void shouldCreateNewSupplierSuccessfully() throws Exception {
        when(supplierService.createSupplier(any())).thenReturn(supplier);

        String json = """
                {
                  "name": "SUPPLIER NAME",
                  "cnpj": "00.000.000/0000-00",
                  "phone": "(00)000000000",
                  "email": "supplier@email.com"
                }
                """;

        mockMvc.perform(post("/suppliers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("SUPPLIER NAME"))
                .andExpect(jsonPath("$.cnpj").value("00.000.000/0000-00"))
                .andExpect(jsonPath("$.phone").value("(00)000000000"))
                .andExpect(jsonPath("$.email").value("supplier@email.com"));
    }

    @Test
    @DisplayName("Should return BadRequest 400 when creating a new supplier with invalid data")
    void shouldReturn400WhenCreatingNewSupplierWithInvalidData() throws Exception {
        String json = """
                {
                  "name": "",
                  "cnpj": "00.000.000/0000-00",
                  "phone": "(00)000000000",
                  "email": "supplieremail.com"
                }
                """;

        mockMvc.perform(post("/suppliers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return Forbidden 403 when creating a new supplier without ADMIN or MANAGER role")
    void shouldReturn403WhenCreatingNewSupplierWithoutAdminOrManagerRole() throws Exception {
        Jwt sellerJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "SELLER")
                .build();

        String json = """
                {
                  "name": "SUPPLIER NAME",
                  "cnpj": "00.000.000/0000-00",
                  "phone": "(00)000000000",
                  "email": "supplier@email.com"
                }
                """;

        mockMvc.perform(post("/suppliers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(sellerJwt)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return Unauthorized 401 when creating a new supplier without JWT")
    void shouldReturn401WhenCreatingNewSupplierWithoutJwt() throws Exception {
        String json = """
                {
                  "name": "SUPPLIER NAME",
                  "cnpj": "00.000.000/0000-00",
                  "phone": "(00)000000000",
                  "email": "supplier@email.com"
                }
                """;

        mockMvc.perform(post("/suppliers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should list all suppliers successfully")
    void shouldListAllSuppliersSuccessfully() throws Exception {
        Page<SupplierListResponse> page = new PageImpl<>(List.of(new SupplierListResponse(supplier), new SupplierListResponse(anotherSupplier)));

        when(supplierService.listAllSuppliers(any())).thenReturn(page);

        mockMvc.perform(get("/suppliers/find-all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(adminJwt))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(supplier.getId()))
                .andExpect(jsonPath("$.content[0].name").value("SUPPLIER NAME"))
                .andExpect(jsonPath("$.content[0].cnpj").value("00.000.000/0000-00"))
                .andExpect(jsonPath("$.content[0].phone").value("(00)000000000"))
                .andExpect(jsonPath("$.content[0].email").value("supplier@email.com"))
                .andExpect(jsonPath("$.content[1].id").value(anotherSupplier.getId()))
                .andExpect(jsonPath("$.content[1].name").value("ANOTHER SUPPLIER NAME"))
                .andExpect(jsonPath("$.content[1].cnpj").value("11.111.111/1111-11"))
                .andExpect(jsonPath("$.content[1].phone").value("(11)111111111"))
                .andExpect(jsonPath("$.content[1].email").value("anothersupplier@email.com"));
    }

    @Test
    @DisplayName("Should update a supplier successfully")
    void shouldUpdateSupplierSuccessfully() throws Exception {
        Supplier updatedSupplier = new Supplier(1L, "SUPPLIER NAME", "00.000.000/0000-00", "(99)999999999", "supplie@email.com", LocalDateTime.now(), null, null, true);
        when(supplierService.updateSupplier(any())).thenReturn(updatedSupplier);

        String json = """
                {
                  "id": 1,
                  "phone": "(99)999999999"
                }
                """;

        mockMvc.perform(put("/suppliers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt))
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(supplier.getId()))
                .andExpect(jsonPath("$.name").value("SUPPLIER NAME"))
                .andExpect(jsonPath("$.cnpj").value("00.000.000/0000-00"))
                .andExpect(jsonPath("$.phone").value("(99)999999999"))
                .andExpect(jsonPath("$.email").value("supplie@email.com"));
    }

    @Test
    @DisplayName("Should disable a supplier successfully")
    void shouldDisableSupplierSuccessfully() throws Exception {
        mockMvc.perform(delete("/suppliers/disable/{id}", supplier.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(adminJwt))
                        .param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should enable a supplier successfully")
    void shouldEnableSupplierSuccessfully() throws Exception {
        when(supplierService.enableSupplier(1L)).thenReturn(supplier);

        mockMvc.perform(patch("/suppliers/enable/{id}", supplier.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(supplier.getId()))
                .andExpect(jsonPath("$.name").value("SUPPLIER NAME"))
                .andExpect(jsonPath("$.cnpj").value("00.000.000/0000-00"))
                .andExpect(jsonPath("$.phone").value("(00)000000000"))
                .andExpect(jsonPath("$.email").value("supplier@email.com"))
                .andExpect(jsonPath("$.active").value(true));
    }
}