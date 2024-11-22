package com.citronix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.citronix.model.Farm;

/**
 * Repository interface for Farm entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface FarmRepository extends JpaRepository<Farm, Long>, JpaSpecificationExecutor<Farm>{

}
