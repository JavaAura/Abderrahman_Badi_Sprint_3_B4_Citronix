package com.citronix.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.citronix.exceptions.InvalidDataException;
import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;
import com.citronix.dto.SaleDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.model.Harvest;
import com.citronix.model.HarvestDetail;
import com.citronix.model.Sale;

@Component
public class HarvestMapper {
    private final List<String> VALID_INCLUDES = Arrays.asList("sale", "harvestDetails",
            "harvestDetails.tree");

    public void verifyIncludes(String... with)
            throws InvalidDataException {
        List<String> includesList = Arrays.asList(with);

        for (String include : includesList) {
            if (!include.isEmpty() && !VALID_INCLUDES.contains(include)) {
                throw new InvalidDataException("Invalid include: " + include);
            }
        }
    }

    public HarvestDTO convertToDTO(Harvest harvest) {
        return HarvestDTO.builder()
                .id(harvest.getId())
                .season(harvest.getSeason())
                .harvestYear(harvest.getHarvestYear())
                .totalYield(harvest.getTotalYield())
                .build();
    }

    public List<HarvestDTO> convertToDTOList(List<Harvest> harvests) {
        return harvests.stream()
                .map(tree -> convertToDTO(tree))
                .collect(Collectors.toList());
    }

    public HarvestDTO convertToDTO(Harvest harvest, String... with) {
        List<String> includesList = Arrays.asList(with);

        List<HarvestDetailDTO> harvestDetailDTOs = null;
        SaleDTO saleDTO = null;

        if (includesList.contains("sale")) {
            Sale sale = harvest.getSale();
            saleDTO = SaleDTO.builder()
                    .id(sale.getId())
                    .client(sale.getClient())
                    .saleDate(sale.getSaleDate())
                    .unitPrice(sale.getUnitPrice())
                    .totalRevenue(sale.getTotalRevenue())
                    .build();
        }

        if (includesList.contains("harvestDetails")) {
            List<HarvestDetail> harvestDetails = harvest.getHarvestDetails();
            harvestDetailDTOs = harvestDetails.stream()
                    .map(harvestDetail -> HarvestDetailDTO.builder().yield(harvestDetail.getYield())
                            .harvestedAt(harvestDetail.getHarvestedAt()).build())
                    .collect(Collectors.toList());
        }

        if (includesList.contains("harvestDetails.tree")) {
            List<HarvestDetail> harvestDetails = harvest.getHarvestDetails();
            harvestDetailDTOs = harvestDetails.stream()
                    .map(harvestDetail -> HarvestDetailDTO.builder().yield(harvestDetail.getYield())
                            .harvestedAt(harvestDetail.getHarvestedAt())
                            .tree(TreeDTO.builder().plantedAt(harvestDetail.getTree().getPlantedAt()).build())
                            .build())
                    .collect(Collectors.toList());
        }

        return HarvestDTO.builder()
                .id(harvest.getId())
                .season(harvest.getSeason())
                .harvestYear(harvest.getHarvestYear())
                .totalYield(harvest.getTotalYield())
                .harvestDetails(harvestDetailDTOs)
                .sale(saleDTO)
                .build();
    }

    public List<HarvestDTO> convertToDTOList(List<Harvest> harvests, String... with) {
        return harvests.stream()
                .map(harvest -> convertToDTO(harvest, with))
                .collect(Collectors.toList());
    }

}
