package com.citronix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.citronix.model.Field;

/**
 * Repository interface for Field entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Query(value = "SELECT CASE WHEN ((f.surface - COALESCE(SUM(fs.surface), 0) + :existingSurface) >= :fieldSurface) THEN TRUE ELSE FALSE END FROM Farm f LEFT JOIN f.fields fs WHERE f.id = :farmId GROUP BY f.id")
    boolean checkFieldSurface(Long farmId, double fieldSurface, double existingSurface);

    @Query("SELECT f.surface FROM Field f WHERE f.id = :fieldId")
    Double getFieldSurface(Long fieldId);

}
