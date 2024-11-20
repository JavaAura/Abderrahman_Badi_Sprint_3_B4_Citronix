package com.citronix.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.citronix.util.HarvestDetailId;
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
@IdClass(HarvestDetailId.class)
@Table(name = "harvest_tree")
@Where(clause = "removed_at IS NULL")
public class HarvestDetail {

    @Id
    @ManyToOne
    @JoinColumn(name = "tree_id")
    private Tree tree;

    @Id
    @ManyToOne
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

    @Column(nullable = true)
    private LocalDateTime removedAt;
}
