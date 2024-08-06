package com.matteusmoreno.moto_manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zipcode;
    private String city;
    private String neighborhood;
    private String state;
    private String street;
    private String number;
    private String complement;
    private LocalDateTime createdAt;
}
