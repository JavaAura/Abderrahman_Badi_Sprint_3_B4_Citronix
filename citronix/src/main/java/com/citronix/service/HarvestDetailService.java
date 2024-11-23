package com.citronix.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.HarvestDetailDTO;
import com.citronix.exceptions.DuplicateResourceException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.HarvestDetailMapper;
import com.citronix.model.HarvestDetail;
import com.citronix.repository.HarvestDetailRepository;
import com.citronix.util.HarvestDetailPk;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for HarvestDetail entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Log4j2
public class HarvestDetailService {
    @Autowired
    private HarvestDetailRepository harvestDetailRepository;

    @Autowired
    private HarvestDetailMapper harvestDetailMapper;

    public HarvestDetailDTO getHarvestDetailById(HarvestDetailPk harvestDetailPk) throws ResourceNotFoundException {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(harvestDetailPk)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest detail not found"));
        return harvestDetailMapper.convertToDTO(harvestDetail);
    }

    public HarvestDetailDTO addHarvestDetail(HarvestDetail harvestDetail) throws DuplicateResourceException {
        if (isTreeHarvested(harvestDetail)) {
            throw new DuplicateResourceException("The tree is already harvested");
        }

        return harvestDetailMapper.convertToDTO(harvestDetailRepository.save(harvestDetail));
    }

    public HarvestDetailDTO updateHarvestDetail(HarvestDetailPk harvestDetailPk, HarvestDetail harvestDetail)
            throws DuplicateResourceException, ResourceNotFoundException {
        boolean skipTest = false;

        HarvestDetail harvestDetailDB = harvestDetailRepository.findById(harvestDetailPk)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest detail not found"));

        if ((harvestDetail.getTree() != null && harvestDetail.getTree().getId() == harvestDetailDB.getTree().getId())
                && (harvestDetail.getHarvest() != null
                        && harvestDetail.getHarvest().getId() == harvestDetailDB.getHarvest().getId())) {
            skipTest = true;
        }
        if (harvestDetail.getTree() == null && harvestDetail.getHarvest() == null) {
            skipTest = true;
        }
        if (harvestDetail.getTree() == null) {
            harvestDetail.setTree(harvestDetailDB.getTree());
        }
        if (harvestDetail.getHarvest() == null) {
            harvestDetail.setHarvest(harvestDetailDB.getHarvest());
        }
        if (Objects.isNull(harvestDetail.getYield())) {
            harvestDetail.setYield(harvestDetailDB.getYield());
        }
        if (Objects.isNull(harvestDetail.getHarvestedAt())) {
            harvestDetail.setHarvestedAt(harvestDetailDB.getHarvestedAt());
        }

        if (!skipTest && isTreeHarvested(harvestDetail)) {
            throw new DuplicateResourceException("The tree is already harvested");
        }

        harvestDetailRepository.delete(harvestDetailDB);
        return harvestDetailMapper.convertToDTO(harvestDetailRepository.save(harvestDetail));
    }

    public void deleteHarvestDetailById(HarvestDetailPk harvestDetailPk) throws ResourceNotFoundException {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(harvestDetailPk)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest detail not found"));
        harvestDetailRepository.delete(harvestDetail);
    }

    public boolean isTreeHarvested(HarvestDetail harvestDetail) {
        return harvestDetailRepository.checkTreeAlreadyHarvested(harvestDetail.getTree().getId(),
                harvestDetail.getHarvest().getId());
    }
}
