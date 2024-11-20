package com.citronix.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.FieldDTO;
import com.citronix.mapper.FieldMapper;
import com.citronix.model.Field;
import com.citronix.repository.FieldRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for Field entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Log4j2
public class FieldService {
    private final List<String> VALID_INCLUDES = Arrays.asList("trainer", "program", "students");

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private FieldMapper fieldMapper;

    public FieldDTO getFieldById(long id, String... with) {
        return null;
    }

    public List<FieldDTO> getAllFields(String... with) {
        return null;
    }

    public FieldDTO addField(Field field) {
        return null;
    }

    public FieldDTO updateField(long fieldId, Field field) {
        return null;
    }

    public FieldDTO deleteFieldById(long fieldId) {
        return null;
    }
}
