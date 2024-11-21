package com.citronix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.FarmDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.FarmMapper;
import com.citronix.model.Farm;
import com.citronix.repository.FarmRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for Farm entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Log4j2
public class FarmService {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FarmMapper farmMapper;

    public FarmDTO getFarmById(long id, String... with) throws ResourceNotFoundException, InvalidDataException {
        farmMapper.verifyIncludes(with);
        Farm farm = farmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Farm not found"));
        return farmMapper.convertToDTO(farm, with);
    }

    public List<FarmDTO> getAllFarms(String... with) {
        return null;
    }

    public FarmDTO addFarm(Farm farm) {
        return null;
    }

    public FarmDTO updateFarm(long farmId, Farm farm) {
        return null;
    }

    public FarmDTO deleteFarmById(long farmId) {
        return null;
    }

}
