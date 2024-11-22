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

import com.citronix.dto.FarmDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Farm;
import com.citronix.service.FarmService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing Farm entities.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController // Marks this class as a RESTful controller.
@RequestMapping("/api/farms")
public class FarmController {
    @Autowired
    private FarmService farmService;

    /**
     * Handles POST requests to save a new farm.
     * 
     * @param the farm entity to be saved
     * @return the saved farm entity
     */
    @Operation(summary = "Create a new farm", description = "Saves a new farm entity to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Farm created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FarmDTO saveFarm(@Valid @RequestBody Farm farm) {
        return farmService.addFarm(farm);
    }

    /**
     * Handles GET requests to fetch the list of all farms.
     * 
     * @return a list of farm entities
     */
    @Operation(summary = "Get all farms", description = "Fetches a list of all farms in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of farms"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<FarmDTO> fetchFarmList() {
        return farmService.getAllFarms();
    }

    /**
     * Handles GET requests to fetch a farm by its id.
     * 
     * @return a farm entity
     */
    @Operation(summary = "Get a farm by ID", description = "Fetches a farm entity by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the farm"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Farm not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public FarmDTO getFarm(
            @Parameter(description = "ID of the farm to be retrieved") @PathVariable("id") Long farmId)
            throws ResourceNotFoundException, InvalidDataException {
        return farmService.getFarmById(farmId, "fields");
    }

    /**
     * Handles PUT requests to update an existing farm.
     * 
     * @param farm   the farm entity with updated information
     * @param farmId the ID of the farm to be updated
     * @return the updated farm entity
     */
    @Operation(summary = "Update an existing farm", description = "Updates a farm entity identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Farm not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public FarmDTO updateFarm(
            @Parameter(description = "Updated farm data") @RequestBody Farm farm,
            @Parameter(description = "ID of the farm to be updated") @PathVariable("id") Long farmId) throws ResourceNotFoundException {
        return farmService.updateFarm(farmId, farm);
    }

    /**
     * Handles DELETE requests to remove a farm by ID.
     * 
     * @param farmId the ID of the farm to be deleted
     * @return a success message
     */
    @Operation(summary = "Delete a farm by ID", description = "Deletes a farm entity identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Farm not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteFarmById(
            @Parameter(description = "ID of the farm to be deleted") @PathVariable("id") Long farmId)
            throws ResourceNotFoundException {
        farmService.deleteFarmById(farmId);
        return "Deleted Successfully";
    }
}
