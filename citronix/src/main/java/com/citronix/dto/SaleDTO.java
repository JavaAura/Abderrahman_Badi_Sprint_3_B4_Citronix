package com.citronix.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private Long id;
    private String client;
    private LocalDate saleDate;
    private Double unitPrice;
    private Double totalRevenue;
    private HarvestDTO harvest;

}
