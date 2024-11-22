package com.citronix.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.citronix.dto.FarmDTO;
import com.citronix.dto.FieldDTO;
import com.citronix.exceptions.InvalidSurfaceException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.FieldMapper;
import com.citronix.model.Farm;
import com.citronix.model.Field;
import com.citronix.repository.FieldRepository;
import com.citronix.service.FieldService;

public class FieldServiceTest {
    @InjectMocks
    private FieldService fieldService;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private FieldMapper fieldMapper;

    private Field field;
    private FieldDTO fieldDTO;
    private Farm farm;
    private FarmDTO farmDTO;

    @BeforeEach
    void setUp() {
        farm = new Farm(1L, null, null, 5000D, null, null, null, null);
        farmDTO = new FarmDTO(1L, null, null, 5000D, null);
        field = new Field(null, 1000D, null, null, null, farm, null);
        fieldDTO = new FieldDTO(null, 1000D, farmDTO, null, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddField() throws InvalidSurfaceException {

        when(fieldRepository.save(field)).thenReturn(field);
        when(fieldMapper.convertToDTO(field)).thenReturn(fieldDTO);
        when(fieldRepository.checkFieldSurface(field.getFarm().getId(), field.getSurface(), 0)).thenReturn(true);
        FieldDTO result = fieldService.addField(field);

        assertNotNull(result);
        assertEquals(1000, result.getSurface());
        verify(fieldRepository, times(1)).save(field);
    }

    @Test
    void testGetAllFields() {
        List<Field> fields = Arrays.asList(
                field,
                new Field(null, 1500D, null, null, null, null, null));

        List<FieldDTO> fieldDTOs = Arrays.asList(
                fieldDTO,
                new FieldDTO(null, 1500D, null, null, null));

        when(fieldRepository.findAll()).thenReturn(fields);

        when(fieldMapper.convertToDTOList(fields)).thenReturn(fieldDTOs);

        List<FieldDTO> result = fieldService.getAllFields();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1500, result.get(1).getSurface());
        verify(fieldRepository, times(1)).findAll();
    }

    @Test
    void testGetFieldById_Success() throws ResourceNotFoundException {
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));

        when(fieldMapper.convertToDTO(field)).thenReturn(fieldDTO);

        FieldDTO result = fieldService.getFieldById(1L);

        assertNotNull(result);
        assertEquals(1000, result.getSurface());
        verify(fieldRepository, times(1)).findById(1L);
    }

    @Test
    void testGetFieldById_NotFound() {
        when(fieldRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fieldService.getFieldById(99L));
        verify(fieldRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateField() throws ResourceNotFoundException, InvalidSurfaceException {

        Field updatedField = new Field(null, 3000D, null, null, null, farm, null);
        FieldDTO updatedFieldDTO = new FieldDTO(null, 3000D, farmDTO, null, null);

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));

        when(fieldRepository.checkFieldSurface(updatedField.getFarm().getId(), updatedField.getSurface(), 0)).thenReturn(true);

        when(fieldRepository.save(field)).thenReturn(updatedField);
        when(fieldMapper.convertToDTO(updatedField)).thenReturn(updatedFieldDTO);

        FieldDTO result = fieldService.updateField(1L, updatedField);

        assertNotNull(result);
        assertEquals(3000, result.getSurface());
        verify(fieldRepository, times(1)).findById(1L);
        verify(fieldRepository, times(1)).save(field);
    }

    @Test
    void testDeleteField() throws ResourceNotFoundException {
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        doNothing().when(fieldRepository).delete(field);

        fieldService.deleteFieldById(1L);

        verify(fieldRepository, times(1)).findById(1L);
        verify(fieldRepository, times(1)).delete(field);
    }

    @Test
    void testDeleteField_NotFound() {
        when(fieldRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> fieldService.deleteFieldById(99L));
        verify(fieldRepository, times(1)).findById(99L);
    }
}
