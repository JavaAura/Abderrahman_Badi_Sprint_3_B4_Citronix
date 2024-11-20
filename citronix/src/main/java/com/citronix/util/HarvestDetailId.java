package com.citronix.util;

import java.io.Serializable;

import com.citronix.model.Harvest;
import com.citronix.model.Tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HarvestDetailId implements Serializable {
    private Tree tree;
    private Harvest harvest;
}