package com.citronix.dto;

import java.util.List;

import com.citronix.model.enums.Season;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HarvestDTO {

    private Long id;
    private Season season;
    private Integer harvestYear;
    private Double totalYield;
    private SaleDTO sale;
    private List<HarvestDetailDTO> harvestDetails;

}
