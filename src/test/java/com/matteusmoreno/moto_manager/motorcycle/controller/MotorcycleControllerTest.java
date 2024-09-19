package com.matteusmoreno.moto_manager.motorcycle.controller;

import com.matteusmoreno.moto_manager.config.SecurityConfiguration;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.response.MotorcycleDetailsResponse;
import com.matteusmoreno.moto_manager.motorcycle.service.MotorcycleService;
import com.matteusmoreno.moto_manager.product.response.ProductDetailsResponse;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MotorcycleController.class)
@DisplayName("Motorcycle Controller Tests")
@Import(SecurityConfiguration.class)
class MotorcycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MotorcycleService motorcycleService;

    private Motorcycle biz100;
    private Motorcycle cg150;
    private Jwt adminJwt;

    @BeforeEach
    void setUp() {
        adminJwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .claim("scope", "ADMIN")
                .build();
        biz100 = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED, "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, true);
        cg150 = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "CG 150", MotorcycleColor.BLACK, "FRT1234", "2015/2016", null, LocalDateTime.now(), null, null, true);
    }

    @Test
    @DisplayName("Should create a motorcycle successfully")
    void shouldCreateMotorcycleSuccessfully() throws Exception {

        when(motorcycleService.createMotorcycle(any())).thenReturn(biz100);

        String json = """
                {
                  "brand": "HONDA",
                  "model": "Biz 100",
                  "color": "RED",
                  "plate": "FRP7898",
                  "year": "2010/2011"
                }
                """;

        mockMvc.perform(post("/motorcycles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().jwt(adminJwt)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(biz100.getId().toString()))
                .andExpect(jsonPath("$.brand").value("HONDA"))
                .andExpect(jsonPath("$.model").value("Biz 100"))
                .andExpect(jsonPath("$.color").value("RED"))
                .andExpect(jsonPath("$.plate").value("FRP7898"))
                .andExpect(jsonPath("$.year").value("2010/2011"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").doesNotExist())
                .andExpect(jsonPath("$.deletedAt").doesNotExist())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should return Forbidden 403 when trying to create a motorcycle without ADMIN scope")
    void shouldReturn403WhenTryingToCreateMotorcycleWithoutAdminScope() throws Exception {
        String json = """
                {
                  "brand": "HONDA",
                  "model": "Biz 100",
                  "color": "RED",
                  "plate": "FRP7898",
                  "year": "2010/2011"
                }
                """;

        mockMvc.perform(post("/motorcycles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().jwt(Jwt.withTokenValue("token")
                        .header("alg", "RS256")
                        .claim("scope", "USER")
                        .build())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return Unauthorized 401 when trying to create a motorcycle without a token")
    void shouldReturn401WhenTryingToCreateMotorcycleWithoutToken() throws Exception {
        String json = """
                {
                  "brand": "HONDA",
                  "model": "Biz 100",
                  "color": "RED",
                  "plate": "FRP7898",
                  "year": "2010/2011"
                }
                """;

        mockMvc.perform(post("/motorcycles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return all motorcycles successfully")
    void shouldReturnAllMotorcyclesSuccessfully() throws Exception {
        Page<MotorcycleDetailsResponse> page = new PageImpl<>(List.of(new MotorcycleDetailsResponse(biz100), new MotorcycleDetailsResponse(cg150)));

        when(motorcycleService.findAllMotorcycles(any())).thenReturn(page);

        mockMvc.perform(get("/motorcycles/find-all")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(biz100.getId().toString()))
                .andExpect(jsonPath("$.content[0].brand").value("HONDA"))
                .andExpect(jsonPath("$.content[0].model").value("Biz 100"))
                .andExpect(jsonPath("$.content[0].color").value("RED"))
                .andExpect(jsonPath("$.content[0].plate").value("FRP7898"))
                .andExpect(jsonPath("$.content[0].year").value("2010/2011"))
                .andExpect(jsonPath("$.content[0].createdAt").exists())
                .andExpect(jsonPath("$.content[0].updatedAt").doesNotExist())
                .andExpect(jsonPath("$.content[0].deletedAt").doesNotExist())
                .andExpect(jsonPath("$.content[0].active").value(true))
                .andExpect(jsonPath("$.content[1].id").value(cg150.getId().toString()))
                .andExpect(jsonPath("$.content[1].brand").value("HONDA"))
                .andExpect(jsonPath("$.content[1].model").value("CG 150"))
                .andExpect(jsonPath("$.content[1].color").value("BLACK"))
                .andExpect(jsonPath("$.content[1].plate").value("FRT1234"))
                .andExpect(jsonPath("$.content[1].year").value("2015/2016"))
                .andExpect(jsonPath("$.content[1].createdAt").exists())
                .andExpect(jsonPath("$.content[1].updatedAt").doesNotExist())
                .andExpect(jsonPath("$.content[1].deletedAt").doesNotExist())
                .andExpect(jsonPath("$.content[1].active").value(true));
    }

    @Test
    @DisplayName("Should update a motorcycle successfully")
    void shouldUpdateMotorcycleSuccessfully() throws Exception {
        Motorcycle updatedBiz100 = new Motorcycle(biz100.getId(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.BLUE, "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, true);

        when(motorcycleService.updateMotorcycle(any())).thenReturn(updatedBiz100);

        String json = """
                {
                  "id": "%s",
                  "color": "BLUE"
                }
                """.formatted(biz100.getId());

        mockMvc.perform(put("/motorcycles/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(biz100.getId().toString()))
                .andExpect(jsonPath("$.brand").value("HONDA"))
                .andExpect(jsonPath("$.model").value("Biz 100"))
                .andExpect(jsonPath("$.color").value("BLUE"))
                .andExpect(jsonPath("$.plate").value("FRP7898"))
                .andExpect(jsonPath("$.year").value("2010/2011"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").doesNotExist())
                .andExpect(jsonPath("$.deletedAt").doesNotExist())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should disable a motorcycle successfully")
    void shouldDisableMotorcycleSuccessfully() throws Exception {
        mockMvc.perform(delete("/motorcycles/disable/{id}", biz100.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(adminJwt))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should enable a motorcycle successfully")
    void shouldEnableMotorcycleSuccessfully() throws Exception {
        when(motorcycleService.enableMotorcycle(any())).thenReturn(biz100);

        mockMvc.perform(patch("/motorcycles/enable/{id}", biz100.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt().jwt(adminJwt)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(biz100.getId().toString()))
                .andExpect(jsonPath("$.brand").value("HONDA"))
                .andExpect(jsonPath("$.model").value("Biz 100"))
                .andExpect(jsonPath("$.color").value("RED"))
                .andExpect(jsonPath("$.plate").value("FRP7898"))
                .andExpect(jsonPath("$.year").value("2010/2011"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").doesNotExist())
                .andExpect(jsonPath("$.deletedAt").doesNotExist())
                .andExpect(jsonPath("$.active").value(true));
    }
}