package com.matteusmoreno.moto_manager.service_order.service_order_product.response;

import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Service Order Product Response Test")
class ServiceOrderProductResponseTest {

    @Test
    @DisplayName("Should create a Service Order Product Response")
    void shouldCreateServiceOrderProductResponse() {
        Product oleoMotor = new Product(2L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        ServiceOrderProduct serviceOrderProductOleo = new ServiceOrderProduct(1L, oleoMotor, 1, oleoMotor.getPrice(), oleoMotor.getPrice().multiply(BigDecimal.ONE), null);

        ServiceOrderProductResponse serviceOrderProductResponse = new ServiceOrderProductResponse(serviceOrderProductOleo);

        assertAll(
                () -> assertEquals(oleoMotor.getName(), serviceOrderProductResponse.productName()),
                () -> assertEquals(oleoMotor.getManufacturer(), serviceOrderProductResponse.productManufacturer()),
                () -> assertEquals(serviceOrderProductOleo.getQuantity(), serviceOrderProductResponse.quantity()),
                () -> assertEquals(serviceOrderProductOleo.getUnitaryPrice(), serviceOrderProductResponse.unitaryPrice()),
                () -> assertEquals(serviceOrderProductOleo.getFinalPrice(), serviceOrderProductResponse.finalPrice())
        );
    }

}