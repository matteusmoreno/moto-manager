package com.matteusmoreno.moto_manager.customer.controller;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.config.SecurityConfiguration;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.response.CustomerDetailsResponse;
import com.matteusmoreno.moto_manager.customer.service.CustomerService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(SecurityConfiguration.class)
@DisplayName("Customer Controller Tests")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private Jwt adminJwt;
    private Customer customer;

    @BeforeEach
    void setUp() {
        adminJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "ADMIN")
                .build();
        Address address = new Address(1L, "28994-675", "City", "Neighborhood", "State", "Street", "21", "Casa", LocalDateTime.now());
        customer = new Customer(UUID.randomUUID(), "Name", "email@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(99)999999999", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        customer.getAddresses().add(address);
    }

    @Test
    @DisplayName("Should create a customer successfully")
    void shouldCreateCustomerSuccessfully() throws Exception {

        when(customerService.createCustomer(any())).thenReturn(customer);

        String json = """
                {
                  "name": "Name",
                  "email": "email@email.com",
                  "birthDate": "1990-08-28",
                  "phone": "(99)999999999",
                  "zipcode": "12345-678",
                  "number": "123",
                  "complement": "Apartment 12"
                }
                """;

        mockMvc.perform(post("/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(customer.getId().toString()))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.birthDate").value("1990-08-28"))
                .andExpect(jsonPath("$.age").value(Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears()))
                .andExpect(jsonPath("$.email").value("email@email.com"))
                .andExpect(jsonPath("$.phone").value("(99)999999999"))
                .andExpect(jsonPath("$.addresses[0].zipcode").value("28994-675"))
                .andExpect(jsonPath("$.addresses[0].city").value("City"))
                .andExpect(jsonPath("$.addresses[0].neighborhood").value("Neighborhood"))
                .andExpect(jsonPath("$.addresses[0].state").value("State"))
                .andExpect(jsonPath("$.addresses[0].street").value("Street"))
                .andExpect(jsonPath("$.addresses[0].number").value("21"))
                .andExpect(jsonPath("$.addresses[0].complement").value("Casa"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").doesNotExist())
                .andExpect(jsonPath("$.deletedAt").doesNotExist())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should return Forbidden 403 when trying to create a motorcycle without correctly scope")
    void shouldReturnForbiddenWhenTryingToCreateCustomerWithoutCorrectlyScope() throws Exception {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "WRONG_SCOPE")
                .build();

        mockMvc.perform(post("/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(jwt)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return Unauthorized 401 when trying to create a motorcycle without a token")
    void shouldReturnUnauthorizedWhenTryingToCreateCustomerWithoutToken() throws Exception {

        String json = """
                {
                  "name": "Name",
                  "email": "email@email.com",
                  "birthDate": "1990-08-28",
                  "phone": "(99)999999999",
                  "zipcode": "12345-678",
                  "number": "123",
                  "complement": "Apartment 12"
                }
                """;

        mockMvc.perform(post("/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return Bad Request 400 when trying to create a customer with invalid data")
    void shouldReturnBadRequestWhenTryingToCreateCustomerWithInvalidData() throws Exception {

        String json = """
                {
                  "name": "",
                  "email": "email@email.com",
                  "birthDate": "1990-08-28",
                  "phone": "(99)999999999",
                  "zipcode": "12345-678",
                  "number": "123",
                  "complement": "Apartment 12"
                }
                """;

        mockMvc.perform(post("/customers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should find all customers successfully")
    void shouldFindAllCustomersSuccessfully() throws Exception {
        Customer customer2 = new Customer(UUID.randomUUID(), "Name 2", "email@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(99)999999999", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        Page<CustomerDetailsResponse> page = new PageImpl<>(List.of(new CustomerDetailsResponse(customer), new CustomerDetailsResponse(customer2)));

        when(customerService.findAllCustomers(any())).thenReturn(page);

        mockMvc.perform(get("/customers/find-all")
                        .with(jwt().jwt(adminJwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(customer.getId().toString()))
                .andExpect(jsonPath("$.content[0].name").value("Name"))
                .andExpect(jsonPath("$.content[0].birthDate").value("1990-08-28"))
                .andExpect(jsonPath("$.content[0].age").value(Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears()))
                .andExpect(jsonPath("$.content[0].email").value("email@email.com"))
                .andExpect(jsonPath("$.content[0].phone").value("(99)999999999"))
                .andExpect(jsonPath("$.content[0].addresses[0].zipcode").value("28994-675"))
                .andExpect(jsonPath("$.content[0].addresses[0].city").value("City"))
                .andExpect(jsonPath("$.content[0].addresses[0].neighborhood").value("Neighborhood"))
                .andExpect(jsonPath("$.content[0].addresses[0].state").value("State"))
                .andExpect(jsonPath("$.content[0].addresses[0].street").value("Street"))
                .andExpect(jsonPath("$.content[0].addresses[0].number").value("21"))
                .andExpect(jsonPath("$.content[0].addresses[0].complement").value("Casa"))
                .andExpect(jsonPath("$.content[0].createdAt").exists())
                .andExpect(jsonPath("$.content[0].updatedAt").doesNotExist())
                .andExpect(jsonPath("$.content[0].deletedAt").doesNotExist())
                .andExpect(jsonPath("$.content[0].active").value(true))

                .andExpect(jsonPath("$.content[1].id").value(customer2.getId().toString()))
                .andExpect(jsonPath("$.content[1].name").value("Name 2"))
                .andExpect(jsonPath("$.content[1].birthDate").value("1990-08-28"))
                .andExpect(jsonPath("$.content[1].age").value(Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears()))
                .andExpect(jsonPath("$.content[1].email").value("email@email.com"))
                .andExpect(jsonPath("$.content[1].phone").value("(99)999999999"))
                .andExpect(jsonPath("$.content[1].addresses").isEmpty())
                .andExpect(jsonPath("$.content[1].createdAt").exists())
                .andExpect(jsonPath("$.content[1].updatedAt").doesNotExist())
                .andExpect(jsonPath("$.content[1].deletedAt").doesNotExist())
                .andExpect(jsonPath("$.content[1].active").value(true));
    }

    @Test
    @DisplayName("Should update a customer successfully")
    void shouldUpdateCustomerSuccessfully() throws Exception {
        Customer updatedCustomer = new Customer(customer.getId(), "Another Name", customer.getEmail(), customer.getBirthDate(), customer.getAge(), customer.getPhone(), customer.getAddresses(), customer.getMotorcycles(), customer.getCreatedAt(), LocalDateTime.now(), null, true);
        when(customerService.updateCustomer(any())).thenReturn(updatedCustomer);

        String json = """
                {
                  "id": "%s",
                  "name": "Another Name"
                }
                """.formatted(customer.getId().toString());

        mockMvc.perform(put("/customers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId().toString()))
                .andExpect(jsonPath("$.name").value("Another Name"))
                .andExpect(jsonPath("$.birthDate").value("1990-08-28"))
                .andExpect(jsonPath("$.age").value(Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears()))
                .andExpect(jsonPath("$.email").value("email@email.com"))
                .andExpect(jsonPath("$.phone").value("(99)999999999"))
                .andExpect(jsonPath("$.addresses[0].zipcode").value("28994-675"))
                .andExpect(jsonPath("$.addresses[0].city").value("City"))
                .andExpect(jsonPath("$.addresses[0].neighborhood").value("Neighborhood"))
                .andExpect(jsonPath("$.addresses[0].state").value("State"))
                .andExpect(jsonPath("$.addresses[0].street").value("Street"))
                .andExpect(jsonPath("$.addresses[0].number").value("21"))
                .andExpect(jsonPath("$.addresses[0].complement").value("Casa"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.deletedAt").doesNotExist())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should disable a customer successfully")
    void shouldDisableCustomerSuccessfully() throws Exception {
        mockMvc.perform(delete("/customers/disable/{id}", customer.getId())
                        .with(jwt().jwt(adminJwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should enable a customer successfully")
    void shouldEnableCustomerSuccessfully() throws Exception {
        when(customerService.enableCustomer(any())).thenReturn(customer);

        mockMvc.perform(patch("/customers/enable/{id}", customer.getId())
                        .with(jwt().jwt(adminJwt))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId().toString()))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.birthDate").value("1990-08-28"))
                .andExpect(jsonPath("$.age").value(Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears()))
                .andExpect(jsonPath("$.email").value("email@email.com"))
                .andExpect(jsonPath("$.phone").value("(99)999999999"))
                .andExpect(jsonPath("$.addresses[0].zipcode").value("28994-675"))
                .andExpect(jsonPath("$.addresses[0].city").value("City"))
                .andExpect(jsonPath("$.addresses[0].neighborhood").value("Neighborhood"))
                .andExpect(jsonPath("$.addresses[0].state").value("State"))
                .andExpect(jsonPath("$.addresses[0].street").value("Street"))
                .andExpect(jsonPath("$.addresses[0].number").value("21"))
                .andExpect(jsonPath("$.addresses[0].complement").value("Casa"));
    }
}