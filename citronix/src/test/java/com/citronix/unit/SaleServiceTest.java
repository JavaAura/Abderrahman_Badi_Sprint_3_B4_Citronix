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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.citronix.dto.SaleDTO;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Sale;
import com.citronix.repository.SaleRepository;
import com.citronix.service.SaleService;

public class SaleServiceTest {
@InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    private Sale sale;

    @BeforeEach
    void setUp() {
        sale = new Sale(1L, "Test client", LocalDate.of(2024, 01, 20), null, null, null, null, null, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddSale() {

        when(saleRepository.save(sale)).thenReturn(sale);

        SaleDTO result = saleService.addSale(sale);

        assertNotNull(result);
        assertEquals("Test client", result.getClient());
        verify(saleRepository, times(1)).save(sale);
    }

    @Test
    void testGetAllSales() {
        List<Sale> sales = Arrays.asList(
                sale,
                new Sale(2L, "Test client 2", LocalDate.of(2024, 12, 20), null, null, null, null, null, null)
        );

        when(saleRepository.findAll()).thenReturn(sales);

        List<SaleDTO> result = saleService.getAllSales();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test client", result.get(0).getClient());
        assertEquals("Test client 2", result.get(1).getClient());
        verify(saleRepository, times(1)).findAll();
    }

     @Test
    void testGetSaleById_Success() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        SaleDTO result = saleService.getSaleById(1L);

        assertNotNull(result);
        assertEquals("Test client", result.getClient());
        verify(saleRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSaleById_NotFound() {
        when(saleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> saleService.getSaleById(99L));
        verify(saleRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateSale() {
        Sale updatedSale = new Sale(1L, "Updated client", LocalDate.of(2024, 11, 20), null, null, null, null, null, null);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleRepository.save(sale)).thenReturn(updatedSale);

        SaleDTO result = saleService.updateSale(1L, updatedSale);

        assertNotNull(result);
        assertEquals("Updated client", result.getClient());
        assertEquals(LocalDate.of(2024, 11, 20), result.getSaleDate());
        verify(saleRepository, times(1)).findById(1L);
        verify(saleRepository, times(1)).save(sale);
    }

    @Test
    void testDeleteSale() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        doNothing().when(saleRepository).delete(sale);

        saleService.deleteSaleById(1L);

        verify(saleRepository, times(1)).findById(1L);
        verify(saleRepository, times(1)).delete(sale);
    }

    @Test
    void testDeleteSale_NotFound() {
        when(saleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> saleService.deleteSaleById(99L));
        verify(saleRepository, times(1)).findById(99L);
    }
}
