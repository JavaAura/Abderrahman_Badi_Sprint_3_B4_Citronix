package com.citronix.util;

import java.io.Serializable;

import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class HarvestDetailPk implements Serializable {

    private Long treeId;
    private Long harvestId;

}