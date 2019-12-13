package com.scholanova.projectstore.repositories;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.*;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.models.Store;


@SpringJUnitConfig(StockRepository.class)
@JdbcTest
public class StockRepositoryTest {
	
	private Integer StoreID = 1;
	
	@Autowired
    private StockRepository stockRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "STOCKS");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "STORES");
    }
    
    @BeforeEach
    void createAll() {
    	
    	Store store = new Store(StoreID, "Citadium");
    	insertStore(store);
    	
    }
    
    @Test
    void whenCreateStock_ThenStockisInDatabaseWithId() {
    	// Given
    	Stock stockToCreate = new Stock(null, StoreID, "apple" , "fruit" , 50);
    	
    	// When
    	Stock createdStore = stockRepository.create(stockToCreate);
    	
    	// Then
    	assertThat(createdStore.getId()).isNotNull();
    	assertThat(createdStore.getName()).isEqualTo(stockToCreate.getName());
    	assertThat(createdStore.getType()).isEqualTo(stockToCreate.getType());
    	assertThat(createdStore.getValue()).isEqualTo(stockToCreate.getValue());

    	
    }
    
    
    @Test
    void WhenGetExistingStock_ThenReturnStock() throws ModelNotFoundException {
    	// Given 
    	Stock stock = new Stock(2, StoreID, "Orange", "fruit", 50);
    	insertStock(stock);
    	
    	// When
        Stock extractedStore = stockRepository.getById(2);
    	
    	// Then
        assertThat(extractedStore).isEqualToComparingFieldByField(stock);
        
    }
    
    @Test
    void WhenGetNotExistingStock_ThenThrowsModeleNotFoundException() throws ModelNotFoundException {
    	// Given 
    	
    	
    	// When
        
    	
    	// Then
        assertThrows(ModelNotFoundException.class, () -> {
        	stockRepository.getById(2);
        	});
        
    }
    
    @Test
    void WhenDeleteExistingStock_ThenNoStockInDatabase() throws ModelNotFoundException {
    	// Given 
    	Stock stock = new Stock(2, StoreID, "Orange", "fruit", 50);
    	insertStock(stock);
    	
    	// When
         stockRepository.deleteById(2);
    	
    	// Then
         assertThrows(ModelNotFoundException.class, () -> {
         	stockRepository.getById(2);
         	});        
    }
    
    @Test
    void WhenDeleteNotExistingStock_ThenThrowsModeleNotFoundException() throws ModelNotFoundException {
    	// Given 
    	
    	
    	// When
        
    	
    	// Then
        assertThrows(ModelNotFoundException.class, () -> {
        	stockRepository.deleteById(2);;
        	});
        
    }
    
    private void insertStore(Store store) {
        String query = "INSERT INTO STORES " +
                "(ID, NAME) " +
                "VALUES ('%d', '%s')";
        jdbcTemplate.execute(
                String.format(query, store.getId(), store.getName()));
    }
    
    private void insertStock(Stock stock) {
        String query = "INSERT INTO STOCKS " +
                "(ID, ID_STORE, NAME, TYPE, VALUE) " +
                "VALUES (%d, %d, '%s', '%s', %d)";
        jdbcTemplate.execute(
                String.format(query, stock.getId(), stock.getId_store(), stock.getName(), stock.getType(), stock.getValue() ));
    }
    
    
    

}
