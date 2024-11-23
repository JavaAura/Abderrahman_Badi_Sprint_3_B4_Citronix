package com.citronix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.citronix.model.Harvest;
import com.citronix.model.enums.Season;

/**
 * Repository interface for Harvest entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(hr) > 0 THEN TRUE ELSE FALSE END FROM Harvest hr WHERE hr.season = :season AND hr.harvestYear = :harvestYear")
    public boolean checkHarvestSeasonExists(Season season, Integer harvestYear);

}
