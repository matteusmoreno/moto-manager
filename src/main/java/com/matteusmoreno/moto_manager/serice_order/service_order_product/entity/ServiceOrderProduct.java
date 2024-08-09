package com.matteusmoreno.moto_manager.serice_order.service_order_product.entity;

import com.matteusmoreno.moto_manager.product.entity.Product;
import com.matteusmoreno.moto_manager.serice_order.entity.ServiceOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "service_orders_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class ServiceOrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    private Integer quantity;
    private BigDecimal unitaryPrice;
    private BigDecimal finalPrice;
    @ManyToOne
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;
}
