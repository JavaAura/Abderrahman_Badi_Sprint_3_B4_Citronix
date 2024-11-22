package com.citronix.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.FarmDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.FarmMapper;
import com.citronix.model.Farm;
import com.citronix.repository.FarmRepository;

import org.apache.commons.lang3.StringUtils;
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

    public FarmDTO getFarmById(long id) throws ResourceNotFoundException {
        Farm farm = farmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Farm not found"));
        return farmMapper.convertToDTO(farm);
    }

    public FarmDTO getFarmById(long id, String... with) throws ResourceNotFoundException, InvalidDataException {
        farmMapper.verifyIncludes(with);
        Farm farm = farmRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Farm not found"));
        log.info("Farm :" + farmMapper.convertToDTO(farm).toString());
        return farmMapper.convertToDTO(farm, with);
    }

    public List<FarmDTO> getAllFarms(String... with) throws InvalidDataException {
        farmMapper.verifyIncludes(with);
        List<Farm> farms = farmRepository.findAll();
        return farmMapper.convertToDTOList(farms, with);
    }

    public List<FarmDTO> getAllFarms() {
        List<Farm> farms = farmRepository.findAll();
        log.info("Farms size is : " + farms.size());
        return farmMapper.convertToDTOList(farms);
    }

    public FarmDTO addFarm(Farm farm) {
        return farmMapper.convertToDTO(farmRepository.save(farm));
    }

    public FarmDTO updateFarm(long farmId, Farm farm) throws ResourceNotFoundException {
        Farm farmDB = farmRepository.findById(farmId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found"));

        if (StringUtils.isNotBlank(farm.getName())) {
            farmDB.setName(farm.getName());
        }
        if (StringUtils.isNotBlank(farm.getAddress())) {
            farmDB.setAddress(farm.getAddress());
        }
        if (Objects.nonNull(farm.getSurface())) {
            farmDB.setSurface(farm.getSurface());
        }

        return farmMapper.convertToDTO(farmRepository.save(farmDB));
    }

    public void deleteFarmById(long farmId) throws ResourceNotFoundException {
        Farm farm = farmRepository.findById(farmId).orElseThrow(() -> new ResourceNotFoundException("Farm not found"));
        farmRepository.delete(farm);
    }

}
