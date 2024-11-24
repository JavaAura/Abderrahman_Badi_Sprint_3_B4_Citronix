package com.citronix.controller;

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

import com.citronix.dto.HarvestDetailDTO;
import com.citronix.exceptions.DuplicateResourceException;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.HarvestDetail;
import com.citronix.service.HarvestDetailService;
import com.citronix.util.HarvestDetailPk;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing HarvestDetail entities.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController // Marks this class as a RESTful controller.
@RequestMapping("/api/harvest-details")
public class HarvestDetailDetailsController {
	@Autowired
	private HarvestDetailService harvestDetailService;

	/**
	 * Handles POST requests to save a new harvest detail.
	 * 
	 * @param the harvest detail entity to be saved
	 * @return the saved harvest detail entity
	 * @throws DuplicateResourceException
	 */
	@Operation(summary = "Create a new harvestDetail", description = "Saves a new harvestDetail entity to the system.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "HarvestDetail created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public HarvestDetailDTO saveHarvestDetail(@Valid @RequestBody HarvestDetail harvestDetail)
			throws DuplicateResourceException {
		return harvestDetailService.addHarvestDetail(harvestDetail);
	}

	/**
	 * Handles GET requests to fetch a harvestDetail by its id.
	 * 
	 * @return a harvestDetail entity
	 * @throws InvalidDataException
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Get a harvestDetail by ID", description = "Fetches a harvestDetail entity by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the harvestDetail"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "404", description = "HarvestDetail not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/harvests/{harvestId}/trees/{treeId}")
	public HarvestDetailDTO getHarvestDetail(
			@Parameter(description = "ID of the harvest to be retrieved") @PathVariable("harvestId") Long harvestId,
			@Parameter(description = "ID of the tree to be retrieved") @PathVariable("treeId") Long treeId)
			throws ResourceNotFoundException, InvalidDataException {

		HarvestDetailPk harvestDetailPk = new HarvestDetailPk();
		harvestDetailPk.setHarvestId(harvestId);
		harvestDetailPk.setTreeId(treeId);

		return harvestDetailService.getHarvestDetailById(harvestDetailPk);
	}

	/**
	 * Handles PUT requests to update an existing harvestDetail.
	 * 
	 * @param harvestDetail   the harvestDetail entity with updated information
	 * @param harvestDetailId the ID of the harvestDetail to be updated
	 * @return the updated harvestDetail entity
	 * @throws DuplicateResourceException
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Update an existing harvestDetail", description = "Updates a harvestDetail entity identified by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "HarvestDetail updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "404", description = "HarvestDetail not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PutMapping("/harvests/{harvestId}/trees/{treeId}")
	public HarvestDetailDTO updateHarvestDetail(
			@Parameter(description = "Updated harvestDetail data") @RequestBody HarvestDetail harvestDetail,
			@Parameter(description = "ID of the harvest to be retrieved") @PathVariable("harvestId") Long harvestId,
			@Parameter(description = "ID of the tree to be retrieved") @PathVariable("treeId") Long treeId)
			throws ResourceNotFoundException, DuplicateResourceException {

		HarvestDetailPk harvestDetailId = new HarvestDetailPk();
		harvestDetailId.setHarvestId(harvestId);
		harvestDetailId.setTreeId(treeId);

		return harvestDetailService.updateHarvestDetail(harvestDetailId, harvestDetail);
	}

	/**
	 * Handles DELETE requests to remove a harvestDetail by ID.
	 * 
	 * @param harvestDetailId the ID of the harvestDetail to be deleted
	 * @return a success message
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Delete a harvestDetail by ID", description = "Deletes a harvestDetail entity identified by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "HarvestDetail deleted successfully"),
			@ApiResponse(responseCode = "404", description = "HarvestDetail not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@DeleteMapping("/harvests/{harvestId}/trees/{treeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteHarvestDetailById(
			@Parameter(description = "ID of the harvest to be retrieved") @PathVariable("harvestId") Long harvestId,
			@Parameter(description = "ID of the tree to be retrieved") @PathVariable("treeId") Long treeId)
			throws ResourceNotFoundException {

		HarvestDetailPk harvestDetailId = new HarvestDetailPk();
		harvestDetailId.setHarvestId(harvestId);
		harvestDetailId.setTreeId(treeId);

		harvestDetailService.deleteHarvestDetailById(harvestDetailId);
		return "Deleted Successfully";
	}
}
