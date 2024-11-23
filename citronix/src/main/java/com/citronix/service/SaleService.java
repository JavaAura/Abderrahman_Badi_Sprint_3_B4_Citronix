package com.citronix.service;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.SaleDTO;
import com.citronix.exceptions.DuplicateResourceException;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.SaleMapper;
import com.citronix.model.Sale;
import com.citronix.repository.HarvestRepository;
import com.citronix.repository.SaleRepository;
import com.citronix.util.SaleServiceHelper;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for Sale entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Log4j2
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private SaleServiceHelper saleServiceHelper;

    public SaleDTO getSaleById(long id) throws ResourceNotFoundException {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return saleMapper.convertToDTO(sale);
    }

    public SaleDTO getSaleById(long id, String... with) throws ResourceNotFoundException, InvalidDataException {
        saleMapper.verifyIncludes(with);
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return saleMapper.convertToDTO(sale, with);
    }

    public List<SaleDTO> getAllSales(String... with) throws InvalidDataException {
        saleMapper.verifyIncludes(with);
        List<Sale> sales = saleRepository.findAll();
        return saleMapper.convertToDTOList(sales, with);
    }

    public List<SaleDTO> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        return saleMapper.convertToDTOList(sales);
    }

    public SaleDTO addSale(Sale sale) throws DuplicateResourceException, ResourceNotFoundException {
        harvestRepository.findById(sale.getHarvest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));
        if (isHarvestSold(sale))
            throw new DuplicateResourceException("A sale already exists with the associated harvest");

        return saleMapper.convertToDTO(saleRepository.save(sale));
    }

    public SaleDTO updateSale(long saleId, Sale sale) throws ResourceNotFoundException, DuplicateResourceException {
        Sale saleDB = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        harvestRepository.findById(sale.getHarvest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found"));

        if (StringUtils.isNotBlank(sale.getClient())) {
            saleDB.setClient(sale.getClient());
        }
        if (Objects.nonNull(sale.getSaleDate())) {
            saleDB.setSaleDate(sale.getSaleDate());
        }
        if (Objects.nonNull(sale.getUnitPrice())) {
            saleDB.setUnitPrice(sale.getUnitPrice());
        }
        if (sale.getHarvest() != null) {
            saleDB.setHarvest(sale.getHarvest());

            if (isHarvestSold(saleDB))
                throw new DuplicateResourceException("A sale already exists with the associated harvest");
        }

        Sale savedSale = saleRepository.save(saleDB);
        saleServiceHelper.refreshEntity(savedSale);
        return saleMapper.convertToDTO(savedSale);

    }

    public void deleteSaleById(long saleId) throws ResourceNotFoundException {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        saleRepository.delete(sale);
    }

    public boolean isHarvestSold(Sale sale) {
        return saleRepository.checkHarvestAvailability(sale.getHarvest().getId());
    }

}
