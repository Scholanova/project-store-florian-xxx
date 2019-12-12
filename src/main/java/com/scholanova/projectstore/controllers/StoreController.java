package com.scholanova.projectstore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.services.StoreService;

@RestController
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping(path = "/stores/{id}")
    public Store getStation() {
        return null;
    }

    @PostMapping(path = "/stores")
    public ResponseEntity<?> createStore(@RequestBody Store store) {
    	try {
    		Store createdStore = storeService.create(store);
    		return ResponseEntity.status(HttpStatus.OK).body(createdStore);
    	} catch (StoreNameCannotBeEmptyException e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("StoreNameCannotBeEmptyException");
    	}
        
    }
    
    
}
