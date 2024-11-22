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
public class FarmDTO {

    @JsonProperty
    private Long id;
    private String name;
    private String address;
    private Double surface;
    private List<FieldDTO> fields;

}
