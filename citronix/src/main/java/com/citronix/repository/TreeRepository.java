package com.citronix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.citronix.model.Tree;

/**
 * Repository interface for Tree entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface TreeRepository extends JpaRepository<Tree, Long>{

    @Query(value = "SELECT CASE WHEN ((f.surface - COALESCE(COUNT(ts) * 100, 0)) >= 100) THEN TRUE ELSE FALSE END FROM Field f LEFT JOIN f.trees ts WHERE f.id = :fieldId GROUP BY f.id")
    boolean checkFieldSurface(Long fieldId);

}
