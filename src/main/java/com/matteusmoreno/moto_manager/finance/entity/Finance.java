package com.matteusmoreno.moto_manager.finance.entity;

import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Finance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reportName;
    private LocalDate reportDate;
    @OneToMany
    private List<Receivable> receivables = new ArrayList<>();
    @OneToMany
    private List<Payable> payables = new ArrayList<>();
    private BigDecimal totalReceivables;
    private BigDecimal totalPayables;
    private BigDecimal profitOrLoss;

}
