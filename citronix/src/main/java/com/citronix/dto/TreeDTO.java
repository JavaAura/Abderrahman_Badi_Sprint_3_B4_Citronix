package com.citronix.dto;

import java.time.LocalDateTime;
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

    private long id;
    private int age;
    private double annualProductivity;
    private LocalDateTime createdAt;
    private FieldDTO field;
    private List<HarvestDetailDTO> harvestDetails;

}
