package com.matteusmoreno.moto_manager.finance.receivable.response;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.finance.constant.PaymentStatus;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Receivable Details Response Tests")
class ReceivableDetailsResponseTest {

    @Test
    @DisplayName("Should return a Receivable Details Response successfully")
    void shouldReturnReceivableDetailsResponseSuccessfully() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        Employee seller = new Employee(UUID.randomUUID(), "seller", "password", "Seller", "seller@email.com", "(99)999999999", LocalDate.of(2000, 2, 10), Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);
        Employee mechanic = new Employee(UUID.randomUUID(), "mechanic", "password", "Mechanic", "mechanic@email.com", "(22)222222222", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "888.888.888-88", EmployeeRole.MECHANIC, address, LocalDateTime.now(), null, null, true);
        Customer customer = new Customer(UUID.randomUUID(), "Customer", "customer@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(11)111111111", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        Motorcycle motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED, "FRP7898", "2010/2011", customer, LocalDateTime.now(), null, null, true);
        Product oleoMotor = new Product(2L, "OLEO DE MOTOR", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        Product ledLamp = new Product(3L, "LED LAMP", "Description", "MANUFACTURER", BigDecimal.TEN, 10, LocalDateTime.now(), null, null, true);
        ServiceOrderProduct serviceOrderProductOleo = new ServiceOrderProduct(1L, oleoMotor, 1, oleoMotor.getPrice(), oleoMotor.getPrice().multiply(BigDecimal.ONE), null);
        ServiceOrderProduct serviceOrderProductLed = new ServiceOrderProduct(2L, ledLamp, 1, ledLamp.getPrice(), ledLamp.getPrice().multiply(BigDecimal.ONE), null);
        ServiceOrder serviceOrder = new ServiceOrder(1L, motorcycle, seller, mechanic, new ArrayList<>(), "troca de óleo e lâmpada de farol", BigDecimal.TEN, BigDecimal.valueOf(30.00), ServiceOrderStatus.PENDING, LocalDateTime.now(), null, null, null, null);

        serviceOrder.getProducts().add(serviceOrderProductOleo);
        serviceOrder.getProducts().add(serviceOrderProductLed);

        Receivable receivable = new Receivable(1L, serviceOrder, serviceOrder.getTotalCost(), serviceOrder.getCreatedAt().toLocalDate(), null, PaymentStatus.PENDING);

        ReceivableDetailsResponse receivableDetailsResponse = new ReceivableDetailsResponse(receivable);

        assertAll("ReceivableDetailsResponse",
                () -> assertEquals(receivable.getId(), receivableDetailsResponse.receivableId()),
                () -> assertEquals(receivable.getServiceOrder().getId(), receivableDetailsResponse.serviceOrderId()),
                () -> assertEquals(receivable.getServiceOrder().getMotorcycle().getModel(), receivableDetailsResponse.motorcycleModel()),
                () -> assertEquals(receivable.getServiceOrder().getMotorcycle().getPlate(), receivableDetailsResponse.motorcyclePlate()),
                () -> assertEquals(receivable.getServiceOrder().getMotorcycle().getCustomer().getName(), receivableDetailsResponse.customerName()),
                () -> assertEquals(receivable.getServiceOrder().getSeller().getName(), receivableDetailsResponse.sellerName()),
                () -> assertEquals(receivable.getServiceOrder().getMechanic().getName(), receivableDetailsResponse.mechanicName()),
                () -> assertEquals(receivable.getServiceOrder().getDescription(), receivableDetailsResponse.description()),
                () -> assertEquals(receivable.getValue(), receivableDetailsResponse.value()),
                () -> assertEquals(receivable.getIssueDate(), receivableDetailsResponse.issueDate()),
                () -> assertEquals(receivable.getPaymentDate(), receivableDetailsResponse.paymentDate()),
                () -> assertEquals(receivable.getStatus().getDisplayName(), receivableDetailsResponse.status())
        );
    }

}