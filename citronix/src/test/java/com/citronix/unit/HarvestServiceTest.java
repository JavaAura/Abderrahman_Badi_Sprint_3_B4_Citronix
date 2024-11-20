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

import com.citronix.dto.HarvestDTO;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Harvest;
import com.citronix.model.enums.Season;
import com.citronix.repository.HarvestRepository;
import com.citronix.service.HarvestService;

public class HarvestServiceTest {
@InjectMocks
    private HarvestService harvestService;

    @Mock
    private HarvestRepository harvestRepository;

    private Harvest harvest;

    @BeforeEach
    void setUp() {
        harvest = new Harvest(1L, Season.SPRING, 2024, 5000D, null, null, null, null, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddHarvest() {

        when(harvestRepository.save(harvest)).thenReturn(harvest);

        HarvestDTO result = harvestService.addHarvest(harvest);

        assertNotNull(result);
        assertEquals(Season.SPRING, result.getSeason());
        verify(harvestRepository, times(1)).save(harvest);
    }

    @Test
    void testGetAllHarvests() {
        List<Harvest> harvests = Arrays.asList(
                harvest,
                new Harvest(null, null, null, null, null, null, null, null, null)
        );

        when(harvestRepository.findAll()).thenReturn(harvests);

        List<HarvestDTO> result = harvestService.getAllHarvests();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Season.SPRING, result.get(0).getSeason());
        verify(harvestRepository, times(1)).findAll();
    }

     @Test
    void testGetHarvestById_Success() {
        when(harvestRepository.findById(1L)).thenReturn(Optional.of(harvest));

        HarvestDTO result = harvestService.getHarvestById(1L);

        assertNotNull(result);
        assertEquals(Season.SPRING, result.getSeason());
        verify(harvestRepository, times(1)).findById(1L);
    }

    @Test
    void testGetHarvestById_NotFound() {
        when(harvestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> harvestService.getHarvestById(99L));
        verify(harvestRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateHarvest() {
        Harvest updatedHarvest = new Harvest(1L, Season.SPRING, 2024, 2000D, null, null, null, null, null);
        when(harvestRepository.findById(1L)).thenReturn(Optional.of(harvest));
        when(harvestRepository.save(harvest)).thenReturn(updatedHarvest);

        HarvestDTO result = harvestService.updateHarvest(1L, updatedHarvest);

        assertNotNull(result);
        assertEquals(Season.SPRING, result.getSeason());
        assertEquals(2000, result.getTotalYield());
        verify(harvestRepository, times(1)).findById(1L);
        verify(harvestRepository, times(1)).save(harvest);
    }

    @Test
    void testDeleteHarvest() {
        when(harvestRepository.findById(1L)).thenReturn(Optional.of(harvest));
        doNothing().when(harvestRepository).delete(harvest);

        harvestService.deleteHarvestById(1L);

        verify(harvestRepository, times(1)).findById(1L);
        verify(harvestRepository, times(1)).delete(harvest);
    }

    @Test
    void testDeleteHarvest_NotFound() {
        when(harvestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> harvestService.deleteHarvestById(99L));
        verify(harvestRepository, times(1)).findById(99L);
    }
}
