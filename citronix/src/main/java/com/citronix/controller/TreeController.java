package com.citronix.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.citronix.dto.TreeDTO;
import com.citronix.exceptions.InsufficientSurfaceException;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Tree;
import com.citronix.service.TreeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing Tree entities.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController // Marks this class as a RESTful controller.
@RequestMapping("/api/trees")
public class TreeController {
	@Autowired
	private TreeService treeService;

	/**
	 * Handles POST requests to save a new tree.
	 * 
	 * @param the tree entity to be saved
	 * @return the saved tree entity
	 * @throws InsufficientSurfaceException
	 */
	@Operation(summary = "Create a new tree", description = "Saves a new tree entity to the system.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Tree created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TreeDTO saveTree(@Valid @RequestBody Tree tree) throws InsufficientSurfaceException {
		return treeService.addTree(tree);
	}

	/**
	 * Handles GET requests to fetch the list of all trees.
	 * 
	 * @return a list of tree entities
	 * @throws InvalidDataException
	 */
	@Operation(summary = "Get all trees", description = "Fetches a list of all trees in the system.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list of trees"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping
	public List<TreeDTO> fetchTreeList() throws InvalidDataException {
		return treeService.getAllTrees("field.farm");
	}

	/**
	 * Handles GET requests to fetch a tree by its id.
	 * 
	 * @return a tree entity
	 * @throws InvalidDataException
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Get a tree by ID", description = "Fetches a tree entity by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the tree"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "404", description = "Tree not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/{id}")
	public TreeDTO getTree(
			@Parameter(description = "ID of the tree to be retrieved") @PathVariable("id") Long treeId)
			throws ResourceNotFoundException, InvalidDataException {
		return treeService.getTreeById(treeId, "harvestDetails.harvest");
	}

	/**
	 * Handles PUT requests to update an existing tree.
	 * 
	 * @param tree   the tree entity with updated information
	 * @param treeId the ID of the tree to be updated
	 * @return the updated tree entity
	 * @throws InsufficientSurfaceException
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Update an existing tree", description = "Updates a tree entity identified by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tree updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "404", description = "Tree not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PutMapping("/{id}")
	public TreeDTO updateTree(
			@Parameter(description = "Updated tree data") @RequestBody Tree tree,
			@Parameter(description = "ID of the tree to be updated") @PathVariable("id") Long treeId)
			throws ResourceNotFoundException, InsufficientSurfaceException {
		return treeService.updateTree(treeId, tree);
	}

	/**
	 * Handles DELETE requests to remove a tree by ID.
	 * 
	 * @param treeId the ID of the tree to be deleted
	 * @return a success message
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Delete a tree by ID", description = "Deletes a tree entity identified by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tree deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Tree not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteTreeById(
			@Parameter(description = "ID of the tree to be deleted") @PathVariable("id") Long treeId)
			throws ResourceNotFoundException {
		treeService.deleteTreeById(treeId);
		return "Deleted Successfully";
	}

}
