package com.citronix.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.HarvestDTO;
import com.citronix.mapper.HarvestMapper;
import com.citronix.model.Harvest;
import com.citronix.repository.HarvestRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for Harvest entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Log4j2
public class HarvestService {
    private final List<String> VALID_INCLUDES = Arrays.asList("trainer", "program", "students");

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private HarvestMapper harvestMapper;

    public HarvestDTO getHarvestById(long id, String... with) {
        return null;
    }

    public List<HarvestDTO> getAllHarvests(String... with) {
        return null;
    }

    public HarvestDTO addHarvest(Harvest harvest) {
        return null;
    }

    public HarvestDTO updateHarvest(long harvestId, Harvest harvest) {
        return null;
    }

    public HarvestDTO deleteHarvestById(long harvestId) {
        return null;
    }
}
