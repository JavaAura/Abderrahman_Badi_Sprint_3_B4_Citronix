package com.citronix.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeDTO {

    private Long id;
    private Integer age;
    private Double annualProductivity;
    private LocalDate plantedAt;
    private FieldDTO field;
    private List<HarvestDetailDTO> harvestDetails;
}
