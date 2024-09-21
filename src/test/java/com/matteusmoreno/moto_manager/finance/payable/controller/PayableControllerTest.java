package com.matteusmoreno.moto_manager.finance.payable.controller;

import com.matteusmoreno.moto_manager.config.SecurityConfiguration;
import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.payable.response.PayableDetailsResponse;
import com.matteusmoreno.moto_manager.finance.payable.service.PayableService;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PayableController.class)
@Import(SecurityConfiguration.class)
@DisplayName("Payable Controller Tests")
class PayableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayableService payableService;

    private Supplier supplier;
    private Payable payable;
    private Jwt adminJwt;

    @BeforeEach
    void setUp() {
        adminJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "ADMIN")
                .build();
        supplier = new Supplier(10L, "Supplier", "99.999.999/0001-99","(99)999999999", "supplier@email.com", LocalDateTime.now(), null, null, true);
        payable = new Payable(1L, supplier, "Description", BigDecimal.valueOf(100.00), LocalDate.of(2021,10,10), LocalDate.of(2021,11,10), null, PaymentStatus.PENDING);


    }

    @Test
    @DisplayName("Should create a payable successfully")
    void shouldCreatePayableSuccessfully() throws Exception {

        when(payableService.createPayable(any())).thenReturn(payable);

        String json = """
                {
                    "supplierId": 10,
                    "description": "Description",
                    "value": 100.00,
                    "issueDate": "2021-10-10",
                    "dueDate": "2021-11-10"
                }
                """;

        mockMvc.perform(post("/payables/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(jwt().jwt(adminJwt)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.supplierName").value("Supplier"))
                .andExpect(jsonPath("$.supplierCnpj").value("99.999.999/0001-99"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.value").value(100.00))
                .andExpect(jsonPath("$.issueDate").value("2021-10-10"))
                .andExpect(jsonPath("$.dueDate").value("2021-11-10"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Should return 400 when create a payable with invalid data")
    void shouldReturn400WhenCreatePayableWithInvalidData() throws Exception {
        String json = """
                {
                    "supplierId": 10,
                    "description": "Description",
                    "value": "CEM",
                    "issueDate": "2021-10-10",
                    "dueDate": "2021-11-10"
                }
                """;

        mockMvc.perform(post("/payables/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(jwt().jwt(adminJwt)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should Forbidden return 403 when create a payable without correctly role")
    void shouldReturnForbidden403WhenCreatePayableWithoutCorrectlyRole() throws Exception {
        Jwt sellerJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "SELLER")
                .build();

        String json = """
                {
                    "supplierId": 10,
                    "description": "Description",
                    "value": 100.00,
                    "issueDate": "2021-10-10",
                    "dueDate": "2021-11-10"
                }
                """;

        mockMvc.perform(post("/payables/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(jwt().jwt(sellerJwt)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return Unauthorized 401 when create a payable without token")
    void shouldReturnUnauthorized401WhenCreatePayableWithoutToken() throws Exception {
        String json = """
                {
                    "supplierId": 10,
                    "description": "Description",
                    "value": 100.00,
                    "issueDate": "2021-10-10",
                    "dueDate": "2021-11-10"
                }
                """;

        mockMvc.perform(post("/payables/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should list all payables successfully")
    void shouldListAllPayablesSuccessfully() throws Exception {
        Payable anotherPayable = new Payable(2L, supplier, "Description 2", BigDecimal.valueOf(200.00), LocalDate.of(2021,10,10), LocalDate.of(2021,11,10), null, PaymentStatus.PENDING);
        Page<PayableDetailsResponse> page = new PageImpl<>(List.of(new PayableDetailsResponse(payable), new PayableDetailsResponse(anotherPayable)));

        when(payableService.findAllPayables(any())).thenReturn(page);

        mockMvc.perform(get("/payables/find-all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt().jwt(adminJwt))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].supplierName").value("Supplier"))
                .andExpect(jsonPath("$.content[0].supplierCnpj").value("99.999.999/0001-99"))
                .andExpect(jsonPath("$.content[0].description").value("Description"))
                .andExpect(jsonPath("$.content[0].value").value(100.00))
                .andExpect(jsonPath("$.content[0].issueDate").value("2021-10-10"))
                .andExpect(jsonPath("$.content[0].dueDate").value("2021-11-10"))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"))

                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].supplierName").value("Supplier"))
                .andExpect(jsonPath("$.content[1].supplierCnpj").value("99.999.999/0001-99"))
                .andExpect(jsonPath("$.content[1].description").value("Description 2"))
                .andExpect(jsonPath("$.content[1].value").value(200.00))
                .andExpect(jsonPath("$.content[1].issueDate").value("2021-10-10"))
                .andExpect(jsonPath("$.content[1].dueDate").value("2021-11-10"))
                .andExpect(jsonPath("$.content[1].status").value("PENDING"));
    }

    @Test
    @DisplayName("Should update a payable successfully")
    void shouldUpdatePayableSuccessfully() throws Exception {
        Payable updatedPayable = new Payable(1L, supplier, "New Description", BigDecimal.valueOf(100.00), LocalDate.of(2021,10,10), LocalDate.of(2021,11,10), null, PaymentStatus.PENDING);

        when(payableService.updatePayable(any())).thenReturn(updatedPayable);

        String json = """
                {
                    "payableId": 1,
                    "description": "New Description"
                }
                """;

        mockMvc.perform(put("/payables/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(jwt().jwt(adminJwt))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.supplierName").value("Supplier"))
                .andExpect(jsonPath("$.supplierCnpj").value("99.999.999/0001-99"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.value").value(100.00))
                .andExpect(jsonPath("$.issueDate").value("2021-10-10"))
                .andExpect(jsonPath("$.dueDate").value("2021-11-10"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Should pay a payable successfully")
    void shouldPayPayableSuccessfully() throws Exception {
        Payable paidPayable = new Payable(1L, supplier, "Description", BigDecimal.valueOf(100.00), LocalDate.of(2021,10,10), LocalDate.of(2021,11,10), LocalDate.now(), PaymentStatus.PAID);

        when(payableService.payPayable(anyLong())).thenReturn(paidPayable);

        mockMvc.perform(patch("/payables/pay/{id}", paidPayable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt().jwt(adminJwt))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.supplierName").value("Supplier"))
                .andExpect(jsonPath("$.supplierCnpj").value("99.999.999/0001-99"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.value").value(100.00))
                .andExpect(jsonPath("$.issueDate").value("2021-10-10"))
                .andExpect(jsonPath("$.dueDate").value("2021-11-10"))
                .andExpect(jsonPath("$.paymentDate").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @DisplayName("Should cancel a payable successfully")
    void shouldCancelPayableSuccessfully() throws Exception {
        Payable canceledPayable = new Payable(1L, supplier, "Description", BigDecimal.valueOf(100.00), LocalDate.of(2021,10,10), LocalDate.of(2021,11,10), LocalDate.now(), PaymentStatus.CANCELED);

        when(payableService.cancelPayable(anyLong())).thenReturn(canceledPayable);

        mockMvc.perform(patch("/payables/cancel/{id}", canceledPayable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(jwt().jwt(adminJwt))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.supplierName").value("Supplier"))
                .andExpect(jsonPath("$.supplierCnpj").value("99.999.999/0001-99"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.value").value(100.00))
                .andExpect(jsonPath("$.issueDate").value("2021-10-10"))
                .andExpect(jsonPath("$.dueDate").value("2021-11-10"))
                .andExpect(jsonPath("$.paymentDate").isNotEmpty())
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }
}