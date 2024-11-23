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

import com.citronix.dto.FieldDTO;
import com.citronix.dto.TreeDTO;
import com.citronix.exceptions.InsufficientSurfaceException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.mapper.TreeMapper;
import com.citronix.model.Field;
import com.citronix.model.Tree;
import com.citronix.repository.TreeRepository;
import com.citronix.service.TreeService;

public class TreeServiceTest {
    @InjectMocks
    private TreeService treeService;

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private TreeMapper treeMapper;

    private Tree tree;
    private Field field;
    private TreeDTO treeDTO;
    private FieldDTO fieldDTO;

    @BeforeEach
    void setUp() {
        field = new Field();
        fieldDTO = new FieldDTO();
        field.setId(1L);
        fieldDTO.setId(1L);
        tree = new Tree(1L, null, null, LocalDate.of(2014, 02, 24), null, null, null, field, null);
        treeDTO = new TreeDTO(1L, null, null, LocalDate.of(2014, 02, 24), fieldDTO, null);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTree() throws InsufficientSurfaceException {

        when(treeRepository.save(tree)).thenReturn(tree);

        when(treeMapper.convertToDTO(tree)).thenReturn(treeDTO);

        when(treeRepository.checkFieldSurface(1L)).thenReturn(true);

        TreeDTO result = treeService.addTree(tree);

        assertNotNull(result);
        assertEquals(LocalDate.of(2014, 02, 24), result.getPlantedAt());
        verify(treeRepository, times(1)).save(tree);
    }

    @Test
    void testGetAllTrees() {
        List<Tree> trees = Arrays.asList(
                tree,
                new Tree(null, null, null, LocalDate.of(2013, 02, 24), null, null, null, null, null));

        List<TreeDTO> treeDTOs = Arrays.asList(
                treeDTO,
                new TreeDTO(null, null, null, LocalDate.of(2013, 02, 24), null, null));

        when(treeRepository.findAll()).thenReturn(trees);

        when(treeMapper.convertToDTOList(trees)).thenReturn(treeDTOs);

        List<TreeDTO> result = treeService.getAllTrees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2014, 02, 24), result.get(0).getPlantedAt());
        verify(treeRepository, times(1)).findAll();
    }

    @Test
    void testGetTreeById_Success() throws ResourceNotFoundException {
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(treeMapper.convertToDTO(tree)).thenReturn(treeDTO);

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
    void testUpdateTree() throws ResourceNotFoundException, InsufficientSurfaceException {
        Tree updatedTree = new Tree(1L, 11, null, LocalDate.of(2013, 02, 24), null, null, null, field, null);
        TreeDTO updatedTreeDTO = new TreeDTO(1L, 11, null, LocalDate.of(2013, 02, 24), fieldDTO, null);

        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(treeRepository.checkFieldSurface(1L)).thenReturn(true);
        
        when(treeRepository.save(tree)).thenReturn(updatedTree);
        when(treeMapper.convertToDTO(updatedTree)).thenReturn(updatedTreeDTO);

        TreeDTO result = treeService.updateTree(1L, updatedTree);

        assertNotNull(result);
        assertEquals(LocalDate.of(2013, 02, 24), result.getPlantedAt());
        assertEquals(11, result.getAge());
        verify(treeRepository, times(1)).findById(1L);
        verify(treeRepository, times(1)).save(tree);
    }

    @Test
    void testDeleteTree() throws ResourceNotFoundException {
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
