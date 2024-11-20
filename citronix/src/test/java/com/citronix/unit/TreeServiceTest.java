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

import com.citronix.dto.TreeDTO;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Tree;
import com.citronix.repository.TreeRepository;
import com.citronix.service.TreeService;

public class TreeServiceTest {
    @InjectMocks
    private TreeService treeService;

    @Mock
    private TreeRepository treeRepository;

    private Tree tree;

    @BeforeEach
    void setUp() {
        tree = new Tree(1L, 10, 200, LocalDate.of(2014, 02, 24), null, null, null, null, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTree() {

        when(treeRepository.save(tree)).thenReturn(tree);

        TreeDTO result = treeService.addTree(tree);

        assertNotNull(result);
        assertEquals(LocalDate.of(2014, 02, 24), result.getPlantedAt());
        verify(treeRepository, times(1)).save(tree);
    }

    @Test
    void testGetAllTrees() {
        List<Tree> trees = Arrays.asList(
                tree,
                new Tree(null, 0, 0, null, null, null, null, null, null));

        when(treeRepository.findAll()).thenReturn(trees);

        List<TreeDTO> result = treeService.getAllTrees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2014, 02, 24), result.get(0).getPlantedAt());
        verify(treeRepository, times(1)).findAll();
    }

    @Test
    void testGetTreeById_Success() {
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));

        TreeDTO result = treeService.getTreeById(1L);

        assertNotNull(result);
        assertEquals(LocalDate.of(2014, 02, 24), result.getPlantedAt());
        verify(treeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTreeById_NotFound() {
        when(treeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> treeService.getTreeById(99L));
        verify(treeRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateTree() {
        Tree updatedTree = new Tree(1L, 11, 200, LocalDate.of(2013, 02, 24), null, null, null, null, null);
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(treeRepository.save(tree)).thenReturn(updatedTree);

        TreeDTO result = treeService.updateTree(1L, updatedTree);

        assertNotNull(result);
        assertEquals(LocalDate.of(2013, 02, 24), result.getPlantedAt());
        assertEquals(11, result.getAge());
        verify(treeRepository, times(1)).findById(1L);
        verify(treeRepository, times(1)).save(tree);
    }

    @Test
    void testDeleteTree() {
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        doNothing().when(treeRepository).delete(tree);

        treeService.deleteTreeById(1L);

        verify(treeRepository, times(1)).findById(1L);
        verify(treeRepository, times(1)).delete(tree);
    }

    @Test
    void testDeleteTree_NotFound() {
        when(treeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> treeService.deleteTreeById(99L));
        verify(treeRepository, times(1)).findById(99L);
    }
}
