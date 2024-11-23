package com.citronix.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.dto.TreeDTO;
import com.citronix.exceptions.InsufficientSurfaceException;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.TreeMapper;
import com.citronix.model.Tree;
import com.citronix.repository.TreeRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Service interface for Tree entity.
 * Defines methods for CRUD operations and additional business logic.
 */
@Service
@Log4j2
public class TreeService {
    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private TreeMapper treeMapper;

    public TreeDTO getTreeById(long id) throws ResourceNotFoundException {
        Tree tree = treeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tree not found"));
        calculateTreeAgeAndAnnualProductivity(tree);
        return treeMapper.convertToDTO(tree);
    }

    public TreeDTO getTreeById(long id, String... with) throws ResourceNotFoundException, InvalidDataException {
        treeMapper.verifyIncludes(with);
        Tree tree = treeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tree not found"));
        calculateTreeAgeAndAnnualProductivity(tree);
        return treeMapper.convertToDTO(tree, with);
    }

    public List<TreeDTO> getAllTrees(String... with) throws InvalidDataException {
        treeMapper.verifyIncludes(with);
        List<Tree> trees = treeRepository.findAll();
        trees.forEach(tree -> calculateTreeAgeAndAnnualProductivity(tree));
        return treeMapper.convertToDTOList(trees, with);
    }

    public List<TreeDTO> getAllTrees() {
        List<Tree> trees = treeRepository.findAll();
        trees.forEach(tree -> calculateTreeAgeAndAnnualProductivity(tree));
        return treeMapper.convertToDTOList(trees);
    }

    public TreeDTO addTree(Tree tree) throws InsufficientSurfaceException {
        if (!isSurfaceSufficent(tree)) {
            throw new InsufficientSurfaceException("Insuffissant surface in the selected field");
        }

        Tree createdTree = treeRepository.save(tree);
        calculateTreeAgeAndAnnualProductivity(createdTree);
        return treeMapper.convertToDTO(createdTree);
    }

    public TreeDTO updateTree(long treeId, Tree tree) throws ResourceNotFoundException, InsufficientSurfaceException {
        Tree treeDB = treeRepository.findById(treeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));

        if (tree.getField() != null) {
            treeDB.setField(tree.getField());
            if (!isSurfaceSufficent(treeDB)) {
                throw new InsufficientSurfaceException("Insuffissant surface in the new field");
            }
        }

        if (Objects.nonNull(tree.getPlantedAt())) {
            treeDB.setPlantedAt(tree.getPlantedAt());
        }

        Tree updatedTree = treeRepository.save(treeDB);
        calculateTreeAgeAndAnnualProductivity(updatedTree);
        return treeMapper.convertToDTO(updatedTree);
    }

    public void deleteTreeById(long treeId) throws ResourceNotFoundException {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));
        treeRepository.delete(tree);
    }

    public boolean isSurfaceSufficent(Tree tree) {
        return treeRepository.checkFieldSurface(tree.getField().getId());
    }

    public Tree calculateTreeAgeAndAnnualProductivity(Tree tree) {
        int age = LocalDate.now().getYear() - tree.getPlantedAt().getYear();
        tree.setAge(age);

        if (age < 3) {
            tree.setAnnualProductivity(2.5D);
        }
        if (age <= 10) {
            tree.setAnnualProductivity(12D);
        }
        if (age > 10) {
            tree.setAnnualProductivity(20D);
        }

        return tree;
    }
}
