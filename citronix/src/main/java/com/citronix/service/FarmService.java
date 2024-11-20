package com.citronix.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.FarmDTO;
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
    private final List<String> VALID_INCLUDES = Arrays.asList("trainer", "program", "students");

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private FarmMapper farmMapper;

    public FarmDTO getFarmById(long id, String... with) {
        return null;
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
