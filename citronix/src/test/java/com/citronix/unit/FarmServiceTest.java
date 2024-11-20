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
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Farm;
import com.citronix.repository.FarmRepository;
import com.citronix.service.FarmService;

public class FarmServiceTest {
    @InjectMocks
    private FarmService farmService;

    @Mock
    private FarmRepository farmRepository;

    private Farm farm;

    @BeforeEach
    void setUp() {
        farm = new Farm(1L, "Test farm", "Test address",2500D, null, null, null, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFarm() {

        when(farmRepository.save(farm)).thenReturn(farm);

        FarmDTO result = farmService.addFarm(farm);

        assertNotNull(result);
        assertEquals("Test farm", result.getName());
        verify(farmRepository, times(1)).save(farm);
    }

    @Test
    void testGetAllFarms() {
        List<Farm> farms = Arrays.asList(
                farm,
                new Farm(2L, "Second farm", "Another address", 3000D, null, null, null, null)
        );

        when(farmRepository.findAll()).thenReturn(farms);

        List<FarmDTO> result = farmService.getAllFarms();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test farm", result.get(0).getName());
        verify(farmRepository, times(1)).findAll();
    }

     @Test
    void testGetFarmById_Success() {
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));

        FarmDTO result = farmService.getFarmById(1L);

        assertNotNull(result);
        assertEquals("Test farm", result.getName());
        verify(farmRepository, times(1)).findById(1L);
    }

    @Test
    void testGetFarmById_NotFound() {
        when(farmRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> farmService.getFarmById(99L));
        verify(farmRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateFarm() {
        Farm updatedFarm = new Farm(1L, "Updated farm", "Updated address", null, null, null, null, null);
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
        when(farmRepository.save(farm)).thenReturn(updatedFarm);

        FarmDTO result = farmService.updateFarm(1L, updatedFarm);

        assertNotNull(result);
        assertEquals("Updated farm", result.getName());
        assertEquals("Updated address", result.getAddress());
        verify(farmRepository, times(1)).findById(1L);
        verify(farmRepository, times(1)).save(farm);
    }

    @Test
    void testDeleteFarm() {
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
        doNothing().when(farmRepository).delete(farm);

        farmService.deleteFarmById(1L);

        verify(farmRepository, times(1)).findById(1L);
        verify(farmRepository, times(1)).delete(farm);
    }

    @Test
    void testDeleteFarm_NotFound() {
        when(farmRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> farmService.deleteFarmById(99L));
        verify(farmRepository, times(1)).findById(99L);
    }
}
