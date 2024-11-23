package com.citronix.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Sale entity in the application.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sales")
@Where(clause = "removed_at IS NULL")
@SQLDelete(sql = "UPDATE sales SET removed_at = CURRENT_TIMESTAMP WHERE id=?")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "client is required")
    private String client;

    @NotNull(message = "sale date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate saleDate;

    @NotNull(message = "unit price is required")
    @Positive(message = "unit price should not be negative")
    private Double unitPrice;

    private Double totalRevenue;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private LocalDateTime removedAt;

    @ManyToOne
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;
}
