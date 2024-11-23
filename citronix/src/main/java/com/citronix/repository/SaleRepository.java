package com.citronix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.citronix.model.Sale;

/**
 * Repository interface for Sale entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{

    @Query(value = "SELECT CASE WHEN (COUNT(s) > 0) THEN TRUE ELSE FALSE END FROM Sale s WHERE s.harvest.id = :harvestId")
    boolean checkHarvestAvailability(Long harvestId);

}
