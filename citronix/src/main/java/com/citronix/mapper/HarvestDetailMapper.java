package com.citronix.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.citronix.dto.HarvestDTO;
import com.citronix.dto.HarvestDetailDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.model.Harvest;
import com.citronix.model.HarvestDetail;
import com.citronix.model.Tree;
import com.citronix.util.TreeServiceHelper;

@Component
public class HarvestDetailMapper {

    @Autowired
    private TreeServiceHelper treeServiceHelper;

    public HarvestDetailDTO convertToDTO(HarvestDetail harvestDetail) {
        Harvest harvest = harvestDetail.getHarvest();
        Tree tree = harvestDetail.getTree();
        treeServiceHelper.calculateTreeAgeAndAnnualProductivity(tree);

        HarvestDTO harvestDTO = HarvestDTO.builder()
                .id(harvest.getId())
                .season(harvest.getSeason())
                .harvestYear(harvest.getHarvestYear())
                .totalYield(harvest.getTotalYield())
                .build();

        TreeDTO treeDTO = TreeDTO.builder()
                .id(tree.getId())
                .plantedAt(tree.getPlantedAt())
                .age(tree.getAge())
                .annualProductivity(tree.getAnnualProductivity())
                .build();

        return HarvestDetailDTO.builder()
                .harvest(harvestDTO)
                .tree(treeDTO)
                .yield(harvestDetail.getYield())
                .harvestedAt(harvestDetail.getHarvestedAt())
                .build();
    }

    public List<HarvestDetailDTO> convertToDTOList(List<HarvestDetail> harvestDetails) {
        return harvestDetails.stream()
                .map(harvestDetail -> convertToDTO(harvestDetail))
                .collect(Collectors.toList());
    }
}
