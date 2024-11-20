package com.citronix.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.citronix.dto.FarmDTO;
import com.citronix.mapper.FarmMapper;
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
        farm = new Farm(1L, "Test farm", "Test address",2500, null, null, null, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDriver() {

        when(farmRepository.save(farm)).thenReturn(farm);

        FarmDTO result = farmService.addFarm(farm);

        assertNotNull(result);
        assertEquals("Test farm", result.getName());
        verify(farmRepository, times(1)).save(farm);
    }
}
