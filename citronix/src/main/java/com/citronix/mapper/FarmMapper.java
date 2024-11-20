package com.citronix.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.citronix.dto.FarmDTO;
import com.citronix.dto.FieldDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.model.Farm;
import com.citronix.model.Field;

@Service
public class FarmMapper {
    private final List<String> VALID_INCLUDES = Arrays.asList("fields");

    public void verifyIncludes(List<String> includesList)
            throws InvalidDataException {

        for (String include : includesList) {
            if (!include.isEmpty() && !VALID_INCLUDES.contains(include)) {
                throw new InvalidDataException("Invalid include: " + include);
            }
        }
    }

    public FarmDTO convertToDTO(Farm farm) {
        return FarmDTO.builder()
                .id(farm.getId())
                .name(farm.getName())
                .address(farm.getAddress())
                .surface(farm.getSurface())
                .build();
    }

    public FarmDTO convertToDTO(Farm farm, List<String> includesList) {
        List<FieldDTO> fieldDTOs = null;

        if (includesList.contains("fields")) {
            List<Field> fields = farm.getFields();
            fieldDTOs = fields.stream().map(field -> FieldDTO.builder()
                    .id(field.getId())
                    .surface(field.getSurface())
                    .treesCount(field.getTrees().size())
                    .build())
                    .collect(Collectors.toList());
        }

        return FarmDTO.builder()
                .id(farm.getId())
                .name(farm.getName())
                .address(farm.getAddress())
                .surface(farm.getSurface())
                .fields(fieldDTOs)
                .build();
    }

    public List<FarmDTO> convertToDTOList(List<Farm> farms, String... with) {
        List<String> includes = Arrays.asList(with);
        return farms.stream()
                .map(farm -> convertToDTO(farm, includes))
                .collect(Collectors.toList());
    }
}
