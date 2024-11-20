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

import com.citronix.dto.FieldDTO;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Field;
import com.citronix.repository.FieldRepository;
import com.citronix.service.FieldService;

public class FieldServiceTest {
    @InjectMocks
    private FieldService fieldService;

    @Mock
    private FieldRepository fieldRepository;

    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field(null, 1000D, null, null, null, null, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddField() {

        when(fieldRepository.save(field)).thenReturn(field);

        FieldDTO result = fieldService.addField(field);

        assertNotNull(result);
        assertEquals(1000, result.getSurface());
        verify(fieldRepository, times(1)).save(field);
    }

    @Test
    void testGetAllFields() {
        List<Field> fields = Arrays.asList(
                field,
                new Field(null, 1500D, null, null, null, null, null)
        );

        when(fieldRepository.findAll()).thenReturn(fields);

        List<FieldDTO> result = fieldService.getAllFields();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1500, result.get(1).getSurface());
        verify(fieldRepository, times(1)).findAll();
    }

     @Test
    void testGetFieldById_Success() {
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));

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
    void testUpdateField() {
        Field updatedField = new Field(null, 3000D, null, null, null, null, null);
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(fieldRepository.save(field)).thenReturn(updatedField);

        FieldDTO result = fieldService.updateField(1L, updatedField);

        assertNotNull(result);
        assertEquals(3000, result.getSurface());
        verify(fieldRepository, times(1)).findById(1L);
        verify(fieldRepository, times(1)).save(field);
    }

    @Test
    void testDeleteField() {
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
