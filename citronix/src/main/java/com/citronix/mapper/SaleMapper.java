package com.citronix.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;
import com.citronix.dto.SaleDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.model.Harvest;
import com.citronix.model.HarvestDetail;
import com.citronix.model.Sale;

@Component
public class SaleMapper {
    private final List<String> VALID_INCLUDES = Arrays.asList("harvest", "harvest.harvestDetails");

    public void verifyIncludes(String... with)
            throws InvalidDataException {
        List<String> includesList = Arrays.asList(with);

        for (String include : includesList) {
            if (!include.isEmpty() && !VALID_INCLUDES.contains(include)) {
                throw new InvalidDataException("Invalid include: " + include);
            }
        }
    }

    public SaleDTO convertToDTO(Sale sale) {
        return SaleDTO.builder()
                .id(sale.getId())
                .client(sale.getClient())
                .saleDate(sale.getSaleDate())
                .unitPrice(sale.getUnitPrice())
                .totalRevenue(sale.getTotalRevenue())
                .build();
    }

    public List<SaleDTO> convertToDTOList(List<Sale> sales) {
        return sales.stream()
                .map(sale -> convertToDTO(sale))
                .collect(Collectors.toList());
    }

    public SaleDTO convertToDTO(Sale sale, String... with) {
        List<String> includesList = Arrays.asList(with);

        HarvestDTO harvestDTO = null;

        if (includesList.contains("harvest")) {
            Harvest harvest = sale.getHarvest();
            harvestDTO = HarvestDTO.builder()
                    .id(harvest.getId())
                    .season(harvest.getSeason())
                    .harvestYear(harvest.getHarvestYear())
                    .totalYield(harvest.getTotalYield())
                    .build();
        }

        if (includesList.contains("harvest.harvestDetails")) {
            Harvest harvest = sale.getHarvest();
            List<HarvestDetail> harvestDetails = harvest.getHarvestDetails();

            List<HarvestDetailDTO> harvestDetailDTOs = harvestDetails.stream()
                    .map(harvestDetail -> HarvestDetailDTO.builder().yield(harvestDetail.getYield())
                            .harvestedAt(harvestDetail.getHarvestedAt()).build())
                    .collect(Collectors.toList());

            harvestDTO = HarvestDTO.builder()
                    .id(harvest.getId())
                    .season(harvest.getSeason())
                    .harvestYear(harvest.getHarvestYear())
                    .totalYield(harvest.getTotalYield())
                    .harvestDetails(harvestDetailDTOs)
                    .build();
        }

        return SaleDTO.builder()
                .id(sale.getId())
                .client(sale.getClient())
                .saleDate(sale.getSaleDate())
                .unitPrice(sale.getUnitPrice())
                .totalRevenue(sale.getTotalRevenue())
                .harvest(harvestDTO)
                .build();
    }

    public List<SaleDTO> convertToDTOList(List<Sale> sales, String... with) {
        return sales.stream()
                .map(sale -> convertToDTO(sale, with))
                .collect(Collectors.toList());
    }
}
