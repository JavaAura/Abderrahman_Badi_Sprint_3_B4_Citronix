package com.citronix.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.citronix.dto.HarvestDetailDTO;
import com.citronix.mapper.HarvestDetailMapper;
import com.citronix.model.HarvestDetail;
import com.citronix.repository.HarvestDetailRepository;
import com.citronix.util.HarvestDetailId;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for HarvestDetail entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Component
@Log4j2
public class HarvestDetailService {
    private final List<String> VALID_INCLUDES = Arrays.asList("trainer", "program", "students");

    @Autowired
    private HarvestDetailRepository harvestDetailRepository;

    @Autowired
    private HarvestDetailMapper harvestDetailMapper;

    public HarvestDetailDTO getHarvestDetailById(long id, String... with) {
        return null;
    }

    public List<HarvestDetailDTO> getAllHarvestDetails(String... with) {
        return null;
    }

    public HarvestDetailDTO addHarvestDetail(HarvestDetail harvestDetail) {
        return null;
    }

    public HarvestDetailDTO updateHarvestDetail(long harvestDetailId, HarvestDetail harvestDetail) {
        return null;
    }

    public HarvestDetailDTO deleteHarvestDetailById(HarvestDetailId harvestDetailId) {
        return null;
    }
}
