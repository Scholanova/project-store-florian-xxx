package com.scholanova.projectstore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scholanova.projectstore.exceptions.StockNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.StockValueInvalidException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.services.StockService;
import com.scholanova.projectstore.services.StoreService;

@RestController
public class StockController {

	
	private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    
    @PostMapping(path = "/stores/{idStore}/stocks")
    public ResponseEntity<?> createStock(@PathVariable Integer idStore, @RequestBody Stock stock) {
		try {
    		Stock createdStock = stockService.create(idStore, stock);
    		return ResponseEntity.status(HttpStatus.OK).body(createdStock);
		} catch ( StockNameCannotBeEmptyException e ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("StockNameCannotBeEmptyException");
		}
		catch ( StockValueInvalidException e ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("StockValueInvalidException");
		}
    }
    
    
	
}
