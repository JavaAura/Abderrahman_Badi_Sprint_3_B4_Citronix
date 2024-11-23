package com.citronix.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.citronix.dto.FarmDTO;
import com.citronix.dto.FieldDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.model.Farm;
import com.citronix.model.Field;
import com.citronix.model.Tree;

@Component
public class FieldMapper {
    private final List<String> VALID_INCLUDES = Arrays.asList("farm", "trees");

    public void verifyIncludes(String... with)
            throws InvalidDataException {
        List<String> includesList = Arrays.asList(with);

        for (String include : includesList) {
            if (!include.isEmpty() && !VALID_INCLUDES.contains(include)) {
                throw new InvalidDataException("Invalid include: " + include);
            }
        }
    }

    public FieldDTO convertToDTO(Field field) {
        return FieldDTO.builder()
                .id(field.getId())
                .surface(field.getSurface())
                .build();
    }

    public List<FieldDTO> convertToDTOList(List<Field> fields) {
        return fields.stream()
                .map(field -> convertToDTO(field))
                .collect(Collectors.toList());
    }

    public FieldDTO convertToDTO(Field field, String... with) {
        List<String> includesList = Arrays.asList(with);

        List<TreeDTO> treeDTOs = null;

        FarmDTO farmDTO = null;

        if (includesList.contains("trees")) {
            List<Tree> trees = field.getTrees();
            treeDTOs = trees.stream()
                    .map(tree -> TreeDTO.builder().id(tree.getId()).plantedAt(tree.getPlantedAt()).build())
                    .collect(Collectors.toList());
        }

        if (includesList.contains("farm")) {
            Farm farm = field.getFarm();
            farmDTO = FarmDTO.builder().id(farm.getId())
                    .name(farm.getName())
                    .address(farm.getAddress())
                    .surface(farm.getSurface()).build();
        }

        return FieldDTO.builder()
                .id(field.getId())
                .surface(field.getSurface())
                .farm(farmDTO)
                .trees(treeDTOs)
                .build();
    }

    public List<FieldDTO> convertToDTOList(List<Field> fields, String... with) {
        return fields.stream()
                .map(field -> convertToDTO(field, with))
                .collect(Collectors.toList());
    }
}
