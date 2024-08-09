package com.matteusmoreno.moto_manager.motorcycle.entity;

import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "motorcycles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Motorcycle {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private MotorcycleBrand brand;
    private String model;
    @Enumerated(EnumType.STRING)
    private MotorcycleColor color;
    private String plate;
    private String year;
    @ManyToOne @JoinColumn(name = "customer_id")
    private Customer customer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean active;

}
