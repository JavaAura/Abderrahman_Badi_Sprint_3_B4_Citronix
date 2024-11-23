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

import com.citronix.dto.HarvestDTO;
import com.citronix.exceptions.DuplicateResourceException;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Harvest;
import com.citronix.service.HarvestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing Harvest entities.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController // Marks this class as a RESTful controller.
@RequestMapping("/api/harvests")
public class HarvestController {
    @Autowired
    private HarvestService harvestService;

    /**
     * Handles POST requests to save a new harvest.
     * 
     * @param the harvest entity to be saved
     * @return the saved harvest entity
     * @throws DuplicateResourceException
     */
    @Operation(summary = "Create a new harvest", description = "Saves a new harvest entity to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Harvest created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HarvestDTO saveHarvest(@Valid @RequestBody Harvest harvest) throws DuplicateResourceException {
        return harvestService.addHarvest(harvest);
    }

    /**
     * Handles GET requests to fetch the list of all harvests.
     * 
     * @return a list of harvest entities
     * @throws InvalidDataException
     * 
     */
    @Operation(summary = "Get all harvests", description = "Fetches a list of all harvests in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of harvests"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<HarvestDTO> fetchHarvestList() throws InvalidDataException {
        return harvestService.getAllHarvests();
    }

    /**
     * Handles GET requests to fetch a harvest by its id.
     * 
     * @return a harvest entity
     * @throws InvalidDataException
     * @throws ResourceNotFoundException
     */
    @Operation(summary = "Get a harvest by ID", description = "Fetches a harvest entity by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the harvest"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Harvest not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public HarvestDTO getHarvest(
            @Parameter(description = "ID of the harvest to be retrieved") @PathVariable("id") Long harvestId)
            throws ResourceNotFoundException, InvalidDataException {
        return harvestService.getHarvestById(harvestId, "harvestDetails.tree");
    }

    /**
     * Handles PUT requests to update an existing harvest.
     * 
     * @param harvest   the harvest entity with updated information
     * @param harvestId the ID of the harvest to be updated
     * @return the updated harvest entity
     * @throws DuplicateResourceException
     * @throws ResourceNotFoundException
     */
    @Operation(summary = "Update an existing harvest", description = "Updates a harvest entity identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Harvest updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Harvest not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public HarvestDTO updateHarvest(
            @Parameter(description = "Updated harvest data") @RequestBody Harvest harvest,
            @Parameter(description = "ID of the harvest to be updated") @PathVariable("id") Long harvestId)
            throws ResourceNotFoundException, DuplicateResourceException {
        return harvestService.updateHarvest(harvestId, harvest);
    }

    /**
     * Handles DELETE requests to remove a harvest by ID.
     * 
     * @param harvestId the ID of the harvest to be deleted
     * @return a success message
     * @throws ResourceNotFoundException
     */
    @Operation(summary = "Delete a harvest by ID", description = "Deletes a harvest entity identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Harvest deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Harvest not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteHarvestById(
            @Parameter(description = "ID of the harvest to be deleted") @PathVariable("id") Long harvestId)
            throws ResourceNotFoundException {
        harvestService.deleteHarvestById(harvestId);
        return "Deleted Successfully";
    }
}
