package com.citronix.util;

import java.io.Serializable;

public class HarvestDetailId implements Serializable {
    private Long tree;
    private Long harvest;

    public HarvestDetailId() {}

    public HarvestDetailId(Long tree, Long harvest) {
        this.tree = tree;
        this.harvest = harvest;
    }

    public Long getHarvest() {
      return this.harvest;
    }
    public void setHarvest(Long value) {
      this.harvest = value;
    }

    public Long getTree() {
      return this.tree;
    }
    public void setTree(Long value) {
      this.tree = value;
    }
}