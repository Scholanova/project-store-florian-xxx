package com.scholanova.projectstore.repositories;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

@SpringJUnitConfig(StoreRepository.class)
@JdbcTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "STORES");
    }

    @Nested
    class Test_getById {

        @Test
        void whenNoStoresWithThatId_thenThrowsException() throws Exception {
            // Given
            Integer id = 1000;

            // When & Then
            assertThrows(ModelNotFoundException.class, () -> {
                storeRepository.getById(id);
            });
        }

        @Test
        void whenStoreExists_thenReturnsTheStore() throws Exception {
            // Given
            Integer id = 1;
            Store store = new Store(id, "Carrefour");
            insertStore(store);

            // When
            Store extractedStore = storeRepository.getById(id);

            // Then
            assertThat(extractedStore).isEqualToComparingFieldByField(store);
        }
    }

    @Nested
    class Test_create {

        @Test
        void whenCreateStore_thenStoreIsInDatabaseWithId() {
            // Given
            String storeName = "Auchan";
            Store storeToCreate = new Store(null, storeName);

            // When
            Store createdStore = storeRepository.create(storeToCreate);

            // Then
            assertThat(createdStore.getId()).isNotNull();
            assertThat(createdStore.getName()).isEqualTo(storeName);
        }
    }
    
    @Nested
    class Test_delete {
    	
    	@Test
        void whenDeleteStore_thenStoreIsNotDatabase() {
            // GIVEN
            String storeName = "Auchan";
            Store existingStore = new Store(1234, storeName);
            
            insertStore(existingStore);
            
            // WHEN
            deleteStore(existingStore);
            
            // THEN
            assertThrows(ModelNotFoundException.class, () -> {
            	storeRepository.getById(1234);
            });
        }
    	
    	@Test
    	void whenDeleteNotExistingStore_thenThrowExceptionModuleNotFoundException() {
    		// GIVEN
            Integer id = 1234;
            
            
            // WHEN
            // THEN
            assertThrows(ModelNotFoundException.class, () -> {
            	storeRepository.deleteById(id);
            });
    	}
    	
    }

    private void insertStore(Store store) {
        String query = "INSERT INTO STORES " +
                "(ID, NAME) " +
                "VALUES ('%d', '%s')";
        jdbcTemplate.execute(
                String.format(query, store.getId(), store.getName()));
    }
    
    private void deleteStore(Store store) {
        String query = "DELETE FROM STORES " +
                "WHERE id = %d ";
        jdbcTemplate.execute(String.format(query, store.getId()));
    }
}