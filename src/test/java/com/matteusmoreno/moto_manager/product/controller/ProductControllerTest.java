package com.matteusmoreno.moto_manager.product.controller;

import com.matteusmoreno.moto_manager.config.SecurityConfiguration;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.product.response.ProductDetailsResponse;
import com.matteusmoreno.moto_manager.product.service.ProductService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@DisplayName("Product Controller Tests")
@Import(SecurityConfiguration.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Jwt adminJwt;
    private Product oleoMotor;
    private Product ledLamp;

    @BeforeEach
    void setUp() {
        adminJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "ADMIN")
                .build();
        oleoMotor = new Product(1L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        ledLamp = new Product(3L, "LED LAMP", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should create a product successfully")
    void shouldCreateProductSuccessfully() throws Exception {

        when(productService.createProduct(any())).thenReturn(oleoMotor);

        String json = """
                {
                  "name": "OLEO DE MOTOR",
                  "description": "Description",
                  "manufacturer": "MANUFACTURER",
                  "price": 10,
                  "quantity": 10
                }
                """;

        mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("OLEO DE MOTOR"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.manufacturer").value("MANUFACTURER"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isEmpty())
                .andExpect(jsonPath("$.deletedAt").isEmpty())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should return Forbidden 403 when the user does not have the necessary authorities")
    void shouldReturn403WhenUserLacksRequiredAuthorities() throws Exception {
        Jwt mechanicJwt = adminJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "MECHANIC")
                .build();

        String json = """
                    {
                      "name": "OLEO DE MOTOR",
                      "description": "Description",
                      "manufacturer": "MANUFACTURER",
                      "price": 10,
                      "quantity": 10
                    }
                    """;

        mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(mechanicJwt)) )
               .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return Unauthorized 401 when the user is not authenticated")
    void shouldReturn401WhenUserIsNotAuthenticated() throws Exception {
        String json = """
                {
                  "name": "OLEO DE MOTOR",
                  "description": "Description",
                  "manufacturer": "MANUFACTURER",
                  "price": 10,
                  "quantity": 10
                }
                """;

        mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return all products successfully")
    void shouldReturnAllProductsSuccessfully() throws Exception {
        Page<ProductDetailsResponse> page = new PageImpl<>(List.of(new ProductDetailsResponse(oleoMotor), new ProductDetailsResponse(ledLamp)));

        when(productService.findAllProducts(any())).thenReturn(page);

        mockMvc.perform(get("/products/find-all")
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("OLEO DE MOTOR"))
                .andExpect(jsonPath("$.content[0].description").value("Description"))
                .andExpect(jsonPath("$.content[0].manufacturer").value("MANUFACTURER"))
                .andExpect(jsonPath("$.content[0].price").value(10))
                .andExpect(jsonPath("$.content[0].quantity").value(10))
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[0].updatedAt").isEmpty())
                .andExpect(jsonPath("$.content[0].deletedAt").isEmpty())
                .andExpect(jsonPath("$.content[0].active").value(true))
                .andExpect(jsonPath("$.content[1].id").value(3))
                .andExpect(jsonPath("$.content[1].name").value("LED LAMP"))
                .andExpect(jsonPath("$.content[1].description").value("Description"))
                .andExpect(jsonPath("$.content[1].manufacturer").value("MANUFACTURER"))
                .andExpect(jsonPath("$.content[1].price").value(10))
                .andExpect(jsonPath("$.content[1].quantity").value(10))
                .andExpect(jsonPath("$.content[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[1].updatedAt").isEmpty())
                .andExpect(jsonPath("$.content[1].deletedAt").isEmpty())
                .andExpect(jsonPath("$.content[1].active").value(true));
    }

    @Test
    @DisplayName("Should update a product successfully")
    void shouldUpdateProductSuccessfully() throws Exception {
        Product updatedOleoMotor = new Product(1L, "OLEO DE MOTOR", "New Description", "NEW MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), LocalDateTime.now(), null, true);

        when(productService.updateProduct(any())).thenReturn(updatedOleoMotor);

        String json = """
                    {
                        "id": 1,
                        "name": "OLEO DE MOTOR",
                        "description": "New Description",
                        "manufacturer": "NEW MANUFACTURER",
                        "price": 10
                    }
                """;

        mockMvc.perform(put("/products/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("OLEO DE MOTOR"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.manufacturer").value("NEW MANUFACTURER"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.deletedAt").isEmpty())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should disable a product successfully")
    void shouldDisableProductSuccessfully() throws Exception {
        Long productId = 1L;
        mockMvc.perform(delete("/products/disable/{1}", productId)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should enable a product successfully")
    void shouldEnableProductSuccessfully() throws Exception {
        when(productService.enableProduct(1L)).thenReturn(oleoMotor);

        mockMvc.perform(patch("/products/enable/{1}", 1)
                        .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("OLEO DE MOTOR"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.manufacturer").value("MANUFACTURER"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isEmpty())
                .andExpect(jsonPath("$.deletedAt").isEmpty())
                .andExpect(jsonPath("$.active").value(true));
    }
}