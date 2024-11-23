package com.citronix.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.citronix.util.HarvestDetailPk;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a HarvestDetail entity in the application.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "harvest_tree")
public class HarvestDetail {

    @EmbeddedId
    private HarvestDetailPk HarvestDetailPk = new HarvestDetailPk();

    @ManyToOne
    @MapsId("treeId")
    @JoinColumn(name = "tree_id")
    private Tree tree;

    @ManyToOne
    @MapsId("harvestId")
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;

    @NotNull(message = "surface is required")
    @Positive(message = "surface should not be negative")
    private Double yield;

    @NotNull(message = "harvest date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate harvestedAt;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = true)
    private LocalDateTime updatedAt;
}
