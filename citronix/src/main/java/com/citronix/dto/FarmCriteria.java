package com.citronix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmCriteria {
    private Long id;
    private String name;
    private String address;
    private Float surface;
}
