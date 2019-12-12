package com.scholanova.projectstore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
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
    public ResponseEntity<?> getStation(@PathVariable Integer id) {
        try {
        	return ResponseEntity.ok().body(storeService.get(id));
        } catch (ModelNotFoundException e) {
        	return ResponseEntity.badRequest().body("ModelNotFoundException");
        }
    }
    
    @DeleteMapping(path = "/stores/{id}")
    public ResponseEntity<?> deleteStation(@PathVariable Integer id) {
    	try {
			storeService.delete(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		} catch (ModelNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ModelNotFoundException");
		}
        
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
    
    @PutMapping(path = "/stores")
    public ResponseEntity<?> putStation(@RequestBody Store store) {
    	try {
	        return ResponseEntity.ok().body(storeService.update(store));
	    } catch (ModelNotFoundException e) {
	    	return ResponseEntity.badRequest().body("ModelNotFoundException");
	    }
    }
    
    
}
