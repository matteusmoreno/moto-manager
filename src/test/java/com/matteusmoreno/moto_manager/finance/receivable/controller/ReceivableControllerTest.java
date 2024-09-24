package com.matteusmoreno.moto_manager.finance.receivable.controller;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.config.SecurityConfiguration;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.finance.receivable.response.ReceivableDetailsResponse;
import com.matteusmoreno.moto_manager.finance.receivable.service.ReceivableService;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReceivableController.class)
@Import(SecurityConfiguration.class)
@DisplayName("Receivable Controller Tests")
class ReceivableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceivableService receivableService;

    private Jwt adminJwt;
    private Receivable receivable;

    @BeforeEach
    void setUp() {
        adminJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "ADMIN")
                .build();
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        Employee seller = new Employee(UUID.randomUUID(), "seller", "password", "Seller", "seller@email.com", "(99)999999999", LocalDate.of(2000, 2, 10), Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);
        Employee mechanic = new Employee(UUID.randomUUID(), "mechanic", "password", "Mechanic", "mechanic@email.com", "(22)222222222", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "888.888.888-88", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
        Customer customer = new Customer(UUID.randomUUID(), "Customer", "customer@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(11)111111111", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        Product oleoMotor = new Product(2L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        Product ledLamp = new Product(3L, "LED LAMP", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        Motorcycle motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED, "FRP7898", "2010/2011", customer, LocalDateTime.now(), null, null, true);

        ServiceOrder serviceOrder = new ServiceOrder(1L, motorcycle, seller, mechanic, new ArrayList<>(), "troca de pneu e óleo", BigDecimal.TEN, BigDecimal.valueOf(30.00), ServiceOrderStatus.PENDING, LocalDateTime.now(), null, null, null, null);
        ServiceOrderProduct oleoMotorServiceOrderProduct = new ServiceOrderProduct(10L, oleoMotor, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);
        ServiceOrderProduct ledLampServiceOrderProduct = new ServiceOrderProduct(20L, ledLamp, 10, BigDecimal.TEN, BigDecimal.TEN.multiply(BigDecimal.TEN), serviceOrder);
        serviceOrder.getProducts().add(oleoMotorServiceOrderProduct);
        serviceOrder.getProducts().add(ledLampServiceOrderProduct);

        receivable = new Receivable(1L, serviceOrder, serviceOrder.getTotalCost(), serviceOrder.getCreatedAt().toLocalDate(), null, PaymentStatus.PENDING);

    }

    @Test
    @DisplayName("Should find all receivables successfully")
    void shouldFindAllReceivablesSuccessfully() throws Exception {
        Page<ReceivableDetailsResponse> page = new PageImpl<>(List.of(new ReceivableDetailsResponse(receivable)));
        when(receivableService.findAllReceivables(any())).thenReturn(page);

        mockMvc.perform(get("/receivables/find-all")
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].receivableId").value(receivable.getId()))
                .andExpect(jsonPath("$.content[0].serviceOrderId").value(receivable.getServiceOrder().getId()))
                .andExpect(jsonPath("$.content[0].motorcycleModel").value(receivable.getServiceOrder().getMotorcycle().getModel()))
                .andExpect(jsonPath("$.content[0].motorcyclePlate").value(receivable.getServiceOrder().getMotorcycle().getPlate()))
                .andExpect(jsonPath("$.content[0].customerName").value(receivable.getServiceOrder().getMotorcycle().getCustomer().getName()))
                .andExpect(jsonPath("$.content[0].sellerName").value(receivable.getServiceOrder().getSeller().getName()))
                .andExpect(jsonPath("$.content[0].mechanicName").value(receivable.getServiceOrder().getMechanic().getName()))
                .andExpect(jsonPath("$.content[0].description").value(receivable.getServiceOrder().getDescription()))
                .andExpect(jsonPath("$.content[0].value").value(receivable.getValue()))
                .andExpect(jsonPath("$.content[0].issueDate").value(receivable.getIssueDate().toString()))
                .andExpect(jsonPath("$.content[0].paymentDate").value(receivable.getPaymentDate()))
                .andExpect(jsonPath("$.content[0].status").value(receivable.getStatus().getDisplayName()));
    }

    @Test
    @DisplayName("Should return Forbidden 403 when trying to paid a receivable without correctly scope")
    void shouldReturnForbiddenWhenTryingToPaidAReceivableWithoutCorrectlyScope() throws Exception {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "SELLER")
                .build();

        mockMvc.perform(patch("/receivables/pay/{id}", receivable.getId())
                        .with(jwt().jwt(jwt))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return Unauthorized 401 when trying to paid a receivable without token")
    void shouldReturnForbiddenWhenTryingToPaidAReceivableWithoutToken() throws Exception {
        mockMvc.perform(patch("/receivables/pay/{id}", receivable.getId()))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Should pay receivable successfully")
    void shouldPayReceivableSuccessfully() throws Exception {
        when(receivableService.payReceivable(receivable.getId())).thenReturn(receivable);

        receivable.setStatus(PaymentStatus.PAID);

        mockMvc.perform(patch("/receivables/pay/{id}", receivable.getId())
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receivableId").value(receivable.getId()))
                .andExpect(jsonPath("$.serviceOrderId").value(receivable.getServiceOrder().getId()))
                .andExpect(jsonPath("$.motorcycleModel").value(receivable.getServiceOrder().getMotorcycle().getModel()))
                .andExpect(jsonPath("$.motorcyclePlate").value(receivable.getServiceOrder().getMotorcycle().getPlate()))
                .andExpect(jsonPath("$.customerName").value(receivable.getServiceOrder().getMotorcycle().getCustomer().getName()))
                .andExpect(jsonPath("$.sellerName").value(receivable.getServiceOrder().getSeller().getName()))
                .andExpect(jsonPath("$.mechanicName").value(receivable.getServiceOrder().getMechanic().getName()))
                .andExpect(jsonPath("$.description").value(receivable.getServiceOrder().getDescription()))
                .andExpect(jsonPath("$.value").value(receivable.getValue()))
                .andExpect(jsonPath("$.issueDate").value(receivable.getIssueDate().toString()))
                .andExpect(jsonPath("$.paymentDate").value(receivable.getPaymentDate()))
                .andExpect(jsonPath("$.status").value("PAID"));
    }
}