package com.citronix.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.citronix.dto.FarmDTO;
import com.citronix.dto.FieldDTO;
import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.model.Farm;
import com.citronix.model.Field;
import com.citronix.model.HarvestDetail;
import com.citronix.model.Tree;

@Component
public class TreeMapper {

    private final List<String> VALID_INCLUDES = Arrays.asList("field", "field.farm", "harvestDetails",
            "harvestDetails.harvest");

    public void verifyIncludes(String... with)
            throws InvalidDataException {
        List<String> includesList = Arrays.asList(with);

        for (String include : includesList) {
            if (!include.isEmpty() && !VALID_INCLUDES.contains(include)) {
                throw new InvalidDataException("Invalid include: " + include);
            }
        }
    }

    public TreeDTO convertToDTO(Tree tree) {
        return TreeDTO.builder()
                .id(tree.getId())
                .plantedAt(tree.getPlantedAt())
                .age(tree.getAge())
                .annualProductivity(tree.getAnnualProductivity())
                .build();
    }

    public List<TreeDTO> convertToDTOList(List<Tree> trees) {
        return trees.stream()
                .map(tree -> convertToDTO(tree))
                .collect(Collectors.toList());
    }

    public TreeDTO convertToDTO(Tree tree, String... with) {
        List<String> includesList = Arrays.asList(with);

        FarmDTO farmDTO = null;
        FieldDTO fieldDTO = null;
        List<HarvestDetailDTO> harvestDetailDTOs = null;

        if (includesList.contains("field")) {
            Field field = tree.getField();
            fieldDTO = FieldDTO.builder()
                    .id(field.getId())
                    .surface(field.getSurface())
                    .build();
        }

        if (includesList.contains("field.farm")) {
            Farm farm = tree.getField().getFarm();
            farmDTO = FarmDTO.builder().id(farm.getId())
                    .name(farm.getName())
                    .address(farm.getAddress())
                    .surface(farm.getSurface()).build();

            Field field = tree.getField();
            fieldDTO = FieldDTO.builder()
                    .id(field.getId())
                    .surface(field.getSurface())
                    .farm(farmDTO)
                    .build();
        }

        if (includesList.contains("harvestDetails")) {
            List<HarvestDetail> harvestDetails = tree.getHarvestDetails();
            harvestDetailDTOs = harvestDetails.stream()
                    .map(harvestDetail -> HarvestDetailDTO.builder().yield(harvestDetail.getYield())
                            .harvestedAt(harvestDetail.getHarvestedAt()).build())
                    .collect(Collectors.toList());
        }

        if (includesList.contains("harvestDetails.harvest")) {
            List<HarvestDetail> harvestDetails = tree.getHarvestDetails();
            harvestDetailDTOs = harvestDetails.stream()
                    .map(harvestDetail -> HarvestDetailDTO.builder().yield(harvestDetail.getYield())
                            .harvestedAt(harvestDetail.getHarvestedAt())
                            .harvest(HarvestDTO.builder().season(harvestDetail.getHarvest().getSeason()).build())
                            .build())
                    .collect(Collectors.toList());
        }

        return TreeDTO.builder()
                .id(tree.getId())
                .plantedAt(tree.getPlantedAt())
                .age(tree.getAge())
                .annualProductivity(tree.getAnnualProductivity())
                .field(fieldDTO)
                .harvestDetails(harvestDetailDTOs)
                .build();
    }

    public List<TreeDTO> convertToDTOList(List<Tree> trees, String... with) {
        return trees.stream()
                .map(tree -> convertToDTO(tree, with))
                .collect(Collectors.toList());
    }

}
