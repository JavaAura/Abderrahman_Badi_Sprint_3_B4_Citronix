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
public class HarvestDetailDTO {

    private Double yield;
    private LocalDate harvestedAt;
    private TreeDTO tree;
    private HarvestDTO harvest;

}
