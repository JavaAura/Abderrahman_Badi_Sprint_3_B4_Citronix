package com.citronix.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.HarvestDTO;
import com.citronix.exceptions.DuplicateResourceException;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
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
    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private HarvestMapper harvestMapper;

    public HarvestDTO getHarvestById(long id) throws ResourceNotFoundException {
        Harvest harvest = harvestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
        return harvestMapper.convertToDTO(harvest);
    }

    public HarvestDTO getHarvestById(long id, String... with) throws ResourceNotFoundException, InvalidDataException {
        harvestMapper.verifyIncludes(with);
        Harvest harvest = harvestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
        return harvestMapper.convertToDTO(harvest, with);
    }

    public List<HarvestDTO> getAllHarvests(String... with) throws InvalidDataException {
        harvestMapper.verifyIncludes(with);
        List<Harvest> harvests = harvestRepository.findAll();
        return harvestMapper.convertToDTOList(harvests, with);
    }

    public List<HarvestDTO> getAllHarvests() {
        List<Harvest> harvests = harvestRepository.findAll();
        return harvestMapper.convertToDTOList(harvests);
    }

    public HarvestDTO addHarvest(Harvest harvest) throws DuplicateResourceException {
        if (isHarvestExists(harvest)) {
            throw new DuplicateResourceException("A harvest already exists for this season");
        }

        Harvest createdHarvest = harvestRepository.save(harvest);
        return harvestMapper.convertToDTO(createdHarvest);
    }

    public HarvestDTO updateHarvest(long harvestId, Harvest harvest) throws ResourceNotFoundException, DuplicateResourceException {
        Harvest harvestDB = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
        
        if (harvest.getSeason() != null) {
            harvestDB.setSeason(harvest.getSeason());
        }

        if (Objects.nonNull(harvest.getHarvestYear())) {
            harvestDB.setHarvestYear(harvest.getHarvestYear());
        }

        if (isHarvestExists(harvestDB)) {
            throw new DuplicateResourceException("A harvest already exists for this season");
        }

        Harvest updatedHarvest = harvestRepository.save(harvestDB);
        return harvestMapper.convertToDTO(updatedHarvest);
    }

    public void deleteHarvestById(long harvestId) throws ResourceNotFoundException {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
        harvestRepository.delete(harvest);
    }

    public boolean isHarvestExists(Harvest harvest){
        return harvestRepository.checkHarvestSeasonExists(harvest.getSeason(), harvest.getHarvestYear());
    }
}
