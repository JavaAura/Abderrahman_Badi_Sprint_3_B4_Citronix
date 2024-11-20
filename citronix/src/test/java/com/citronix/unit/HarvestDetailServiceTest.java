package com.citronix.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.citronix.dto.HarvestDetailDTO;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Harvest;
import com.citronix.model.HarvestDetail;
import com.citronix.model.Tree;
import com.citronix.model.enums.Season;
import com.citronix.repository.HarvestDetailRepository;
import com.citronix.service.HarvestDetailService;
import com.citronix.util.HarvestDetailId;

public class HarvestDetailServiceTest {
    @InjectMocks
    private HarvestDetailService harvestDetailService;

    @Mock
    private HarvestDetailRepository harvestDetailRepository;

    private static HarvestDetail harvestDetail;
    private static Harvest harvest;
    private static Tree tree;

    @BeforeAll
    static void init(){
        harvest = new Harvest(1L, Season.SPRING, null, null, null, null, null, null, null);
        tree = new Tree(1l, 0, 0, null, null, null, null, null, null);
        harvestDetail = new HarvestDetail(tree, harvest, 100D, LocalDate.of(2024, 10, 25), null, null, null);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddHarvestDetail() {

        when(harvestDetailRepository.save(harvestDetail)).thenReturn(harvestDetail);

        HarvestDetailDTO result = harvestDetailService.addHarvestDetail(harvestDetail);

        assertNotNull(result);
        assertEquals(Season.SPRING, result.getHarvest().getSeason());
        assertEquals(100D, result.getYield());
        verify(harvestDetailRepository, times(1)).save(harvestDetail);
    }

    @Test
    void testGetAllHarvestDetails() {
        List<HarvestDetail> harvestDetails = Arrays.asList(
                harvestDetail,
                new HarvestDetail(null, null, null, null, null, null, null));

        when(harvestDetailRepository.findAll()).thenReturn(harvestDetails);

        List<HarvestDetailDTO> result = harvestDetailService.getAllHarvestDetails();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Season.SPRING, result.get(0).getHarvest().getSeason());
        assertEquals(100D, result.get(0).getYield());
        verify(harvestDetailRepository, times(1)).findAll();
    }

    @Test
    void testGetHarvestDetailById_Success() {
        when(harvestDetailRepository.findById(new HarvestDetailId(tree, harvest))).thenReturn(Optional.of(harvestDetail));

        HarvestDetailDTO result = harvestDetailService.getHarvestDetailById(1L);

        assertNotNull(result);
        assertEquals(Season.SPRING, result.getHarvest().getSeason());
        assertEquals(100D, result.getYield());
        verify(harvestDetailRepository, times(1)).findById(new HarvestDetailId(tree, harvest));
    }

    @Test
    void testGetHarvestDetailById_NotFound() {
        when(harvestDetailRepository.findById(new HarvestDetailId(null, null))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> harvestDetailService.getHarvestDetailById(99L));
        verify(harvestDetailRepository, times(1)).findById(new HarvestDetailId(null, null));
    }

    @Test
    void testUpdateHarvestDetail() {
        HarvestDetail updatedHarvestDetail = new HarvestDetail(tree, harvest, 200D, LocalDate.of(2024, 10, 25), null, null, null);
        when(harvestDetailRepository.findById(new HarvestDetailId(tree, harvest))).thenReturn(Optional.of(harvestDetail));
        when(harvestDetailRepository.save(harvestDetail)).thenReturn(updatedHarvestDetail);

        HarvestDetailDTO result = harvestDetailService.updateHarvestDetail(1L, updatedHarvestDetail);

        assertNotNull(result);
        assertEquals(Season.SPRING, result.getHarvest().getSeason());
        assertEquals(100D, result.getYield());
        verify(harvestDetailRepository, times(1)).findById(new HarvestDetailId(tree, harvest));
        verify(harvestDetailRepository, times(1)).save(harvestDetail);
    }

    @Test
    void testDeleteHarvestDetail() {
        when(harvestDetailRepository.findById(new HarvestDetailId(tree, harvest))).thenReturn(Optional.of(harvestDetail));
        doNothing().when(harvestDetailRepository).delete(harvestDetail);

        harvestDetailService.deleteHarvestDetailById(new HarvestDetailId(tree, harvest));

        verify(harvestDetailRepository, times(1)).findById(new HarvestDetailId(tree, harvest));
        verify(harvestDetailRepository, times(1)).delete(harvestDetail);
    }

    @Test
    void testDeleteHarvestDetail_NotFound() {
        when(harvestDetailRepository.findById(new HarvestDetailId(null, null))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> harvestDetailService.deleteHarvestDetailById(new HarvestDetailId(null, null)));
        verify(harvestDetailRepository, times(1)).findById(new HarvestDetailId(null, null));
    }
}
