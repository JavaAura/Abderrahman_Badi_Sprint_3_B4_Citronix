package com.citronix.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO {

    private Long id;
    @JsonProperty
    private Double surface;
    private FarmDTO farm;
    private Integer treesCount;
    private List<TreeDTO> trees;

}
