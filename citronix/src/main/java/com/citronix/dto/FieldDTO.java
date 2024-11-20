package com.citronix.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO {

    private long id;
    private double surface;
    private FarmDTO farm;
    private List<TreeDTO> trees;

}
