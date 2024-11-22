package com.citronix.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.citronix.dto.SaleDTO;
import com.citronix.mapper.SaleMapper;
import com.citronix.model.Sale;
import com.citronix.repository.SaleRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for Sale entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Component
@Log4j2
public class SaleService {
    private final List<String> VALID_INCLUDES = Arrays.asList("trainer", "program", "students");

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleMapper saleMapper;

    public SaleDTO getSaleById(long id, String... with) {
        return null;
    }

    public List<SaleDTO> getAllSales(String... with) {
        return null;
    }

    public SaleDTO addSale(Sale sale) {
        return null;
    }

    public SaleDTO updateSale(long saleId, Sale sale) {
        return null;
    }

    public SaleDTO deleteSaleById(long saleId) {
        return null;
    }
}
