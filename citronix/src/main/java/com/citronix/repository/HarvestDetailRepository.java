package com.citronix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.citronix.model.HarvestDetail;
import com.citronix.util.HarvestDetailPk;

/**
 * Repository interface for HarvestDetail entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, HarvestDetailPk> {

    @Query(value = "SELECT CASE WHEN COUNT(hd) > 0 THEN TRUE ELSE FALSE END FROM HarvestDetail hd WHERE hd.tree.id = :treeId AND hd.harvest.id = :harvestId")
    boolean checkTreeAlreadyHarvested(Long treeId, Long harvestId);

}
