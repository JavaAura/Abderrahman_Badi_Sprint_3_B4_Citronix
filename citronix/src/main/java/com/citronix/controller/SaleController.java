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

import com.citronix.dto.SaleDTO;
import com.citronix.exceptions.DuplicateResourceException;
import com.citronix.exceptions.InvalidDataException;
import com.citronix.exceptions.ResourceNotFoundException;
import com.citronix.model.Sale;
import com.citronix.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * REST controller for managing Sale entities.
 * Handles HTTP requests and routes them to the appropriate service methods.
 */
@RestController // Marks this class as a RESTful controller.
@RequestMapping("/api/sales")
public class SaleController {
	@Autowired
	private SaleService saleService;

	/**
	 * Handles POST requests to save a new sale.
	 * 
	 * @param the sale entity to be saved
	 * @return the saved sale entity
	 * @throws DuplicateResourceException
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Create a new sale", description = "Saves a new sale entity to the system.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Sale created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SaleDTO saveSale(@Valid @RequestBody Sale sale)
			throws DuplicateResourceException, ResourceNotFoundException {
		return saleService.addSale(sale);
	}

	/**
	 * Handles GET requests to fetch the list of all sales.
	 * 
	 * @return a list of sale entities
	 * @throws InvalidDataException
	 */
	@Operation(summary = "Get all sales", description = "Fetches a list of all sales in the system.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved list of sales"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping
	public List<SaleDTO> fetchSaleList() throws InvalidDataException {
		return saleService.getAllSales("harvest");
	}

	/**
	 * Handles GET requests to fetch a sale by its id.
	 * 
	 * @return a sale entity
	 * @throws ResourceNotFoundException
	 * @throws InvalidDataException
	 */
	@Operation(summary = "Get a sale by ID", description = "Fetches a sale entity by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the sale"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "404", description = "Sale not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/{id}")
	public SaleDTO getSale(
			@Parameter(description = "ID of the sale to be retrieved") @PathVariable("id") Long saleId)
			throws ResourceNotFoundException, InvalidDataException {
		return saleService.getSaleById(saleId, "harvest.harvestDetails");
	}

	/**
	 * Handles PUT requests to update an existing sale.
	 * 
	 * @param sale   the sale entity with updated information
	 * @param saleId the ID of the sale to be updated
	 * @return the updated sale entity
	 * @throws DuplicateResourceException
	 * @throws ResourceNotFoundException
	 * 
	 */
	@Operation(summary = "Update an existing sale", description = "Updates a sale entity identified by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sale updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "404", description = "Sale not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PutMapping("/{id}")
	public SaleDTO updateSale(
			@Parameter(description = "Updated sale data") @RequestBody Sale sale,
			@Parameter(description = "ID of the sale to be updated") @PathVariable("id") Long saleId)
			throws ResourceNotFoundException, DuplicateResourceException {
		return saleService.updateSale(saleId, sale);
	}

	/**
	 * Handles DELETE requests to remove a sale by ID.
	 * 
	 * @param saleId the ID of the sale to be deleted
	 * @return a success message
	 * @throws ResourceNotFoundException
	 */
	@Operation(summary = "Delete a sale by ID", description = "Deletes a sale entity identified by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Sale deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Sale not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteSaleById(
			@Parameter(description = "ID of the sale to be deleted") @PathVariable("id") Long saleId)
			throws ResourceNotFoundException {
		saleService.deleteSaleById(saleId);
		return "Deleted Successfully";
	}
}
