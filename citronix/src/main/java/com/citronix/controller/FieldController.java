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

import com.citronix.dto.FieldDTO;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.InvalidSurfaceException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Field;
import com.citronix.service.FieldService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing Field entities.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController // Marks this class as a RESTful controller.
@RequestMapping("/api/fields")
public class FieldController {
    @Autowired
    private FieldService fieldService;

    /**
     * Handles POST requests to save a new field.
     * 
     * @param the field entity to be saved
     * @return the saved field entity
     */
    @Operation(summary = "Create a new field", description = "Saves a new field entity to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Field created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FieldDTO saveField(@Valid @RequestBody Field field) throws InvalidSurfaceException {
        return fieldService.addField(field);
    }

    /**
     * Handles GET requests to fetch the list of all fields.
     * 
     * @return a list of field entities
     */
    @Operation(summary = "Get all fields", description = "Fetches a list of all fields in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of fields"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<FieldDTO> fetchFieldList() throws InvalidDataException {
        return fieldService.getAllFields("farm");
    }

    /**
     * Handles GET requests to fetch a field by its id.
     * 
     * @return a field entity
     */
    @Operation(summary = "Get a field by ID", description = "Fetches a field entity by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the field"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Field not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public FieldDTO getField(
            @Parameter(description = "ID of the field to be retrieved") @PathVariable("id") Long fieldId)
            throws ResourceNotFoundException, InvalidDataException {
        return fieldService.getFieldById(fieldId, "farm", "trees");
    }


    /**
     * Handles PUT requests to update an existing field.
     * 
     * @param field   the field entity with updated information
     * @param fieldId the ID of the field to be updated
     * @return the updated field entity
     */
    @Operation(summary = "Update an existing field", description = "Updates a field entity identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Field not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public FieldDTO updateField(
            @Parameter(description = "Updated field data") @RequestBody Field field,
            @Parameter(description = "ID of the field to be updated") @PathVariable("id") Long fieldId) throws ResourceNotFoundException, InvalidSurfaceException {
        return fieldService.updateField(fieldId, field);
    }

    /**
     * Handles DELETE requests to remove a field by ID.
     * 
     * @param fieldId the ID of the field to be deleted
     * @return a success message
     */
    @Operation(summary = "Delete a field by ID", description = "Deletes a field entity identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Field not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteFieldById(
            @Parameter(description = "ID of the field to be deleted") @PathVariable("id") Long fieldId)
            throws ResourceNotFoundException {
        fieldService.deleteFieldById(fieldId);
        return "Deleted Successfully";
    }
}
