package com.matteusmoreno.moto_manager.service_order.entity;

import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.service_order.constant.ServiceOrderStatus;
import com.matteusmoreno.moto_manager.service_order.service_order_product.entity.ServiceOrderProduct;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class ServiceOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Motorcycle motorcycle;

    @ManyToOne
    private Employee seller;

    @ManyToOne
    private Employee mechanic;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOrderProduct> products = new ArrayList<>();  // Certifique-se de que a lista é mutável

    private String description;
    private BigDecimal laborPrice;
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    private ServiceOrderStatus serviceOrderStatus;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime canceledAt;

}
