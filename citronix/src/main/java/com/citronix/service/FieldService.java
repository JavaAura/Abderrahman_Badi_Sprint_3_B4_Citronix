package com.citronix.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.citronix.dto.FieldDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.InvalidSurfaceException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.FieldMapper;
import com.citronix.model.Field;
import com.citronix.repository.FieldRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for Field entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Component
@Log4j2
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldMapper fieldMapper;

    public FieldDTO getFieldById(long id) throws ResourceNotFoundException {
        Field field = fieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        return fieldMapper.convertToDTO(field);
    }

    public FieldDTO getFieldById(long id, String... with) throws ResourceNotFoundException, InvalidDataException {
        fieldMapper.verifyIncludes(with);
        Field field = fieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        return fieldMapper.convertToDTO(field, with);
    }

    public List<FieldDTO> getAllFields(String... with) throws InvalidDataException {
        fieldMapper.verifyIncludes(with);
        List<Field> fields = fieldRepository.findAll();
        return fieldMapper.convertToDTOList(fields, with);
    }

    public List<FieldDTO> getAllFields() {
        List<Field> fields = fieldRepository.findAll();
        return fieldMapper.convertToDTOList(fields);
    }

    public FieldDTO addField(Field field) throws InvalidSurfaceException {
        if (!isFieldSurfaceValid(field))
            throw new InvalidSurfaceException("Surface provided surpasses farm capacity");
        return fieldMapper.convertToDTO(fieldRepository.save(field));
    }

    public FieldDTO updateField(long fieldId, Field field) throws ResourceNotFoundException, InvalidSurfaceException {
        Field fieldDB = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));

        if (field.getFarm() != null) {
            fieldDB.setFarm(field.getFarm());
        }

        if (Objects.nonNull(field.getSurface())) {
            fieldDB.setSurface(field.getSurface());
        }

        if (field.getSurface() != null && !isFieldSurfaceValid(fieldDB))
            throw new InvalidSurfaceException("Surface provided surpasses farm capacity");

        return fieldMapper.convertToDTO(fieldRepository.save(fieldDB));
    }

    public void deleteFieldById(long fieldId) throws ResourceNotFoundException {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        fieldRepository.delete(field);
    }

    public boolean isFieldSurfaceValid(Field field) {
        return fieldRepository.checkFieldSurface(field.getFarm().getId(), field.getSurface().doubleValue(),
                field.getId() == null ? 0 : fieldRepository.getFieldSurface(field.getId()).doubleValue());
    }

}
