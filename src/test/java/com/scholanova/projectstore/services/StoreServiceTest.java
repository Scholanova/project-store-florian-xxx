package com.scholanova.projectstore.services;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @BeforeEach
    void setUp() {
        storeService = new StoreService(storeRepository);
    }
    
    @Test
    void givenExistingStoreId_whenCallGetStoreById_thenReturnExistingStore() throws ModelNotFoundException, StoreNameCannotBeEmptyException {
    	// GIVEN
        Integer id = 1234;
        String name = "BHV";
        
        Store existingStore = new Store(id, name);
        when(storeRepository.getById(id)).thenReturn(existingStore);

        // WHEN
        Store returnedStore = storeService.get(id);

        // THEN
        verify(storeRepository, atLeastOnce()).getById(id);
        assertThat(returnedStore).isEqualTo(existingStore);
    }
    
    @Test
    void givenNotExistingStoreId_whenCallGetStoreById_thenThrowModelNotFoundException() throws ModelNotFoundException, StoreNameCannotBeEmptyException {
    	// GIVEN
        Integer id = 1234;
        
        when(storeRepository.getById(id)).thenThrow(new ModelNotFoundException());
        
        // WHEN
        // THEN
        assertThrows(ModelNotFoundException.class, () -> {
        	storeService.get(id);
        });
    }

    @Test
    void givenNoStoreName_whenCreated_failsWithNoEmptyStoreNameError() {
        // GIVEN
        Integer id = null;
        String name = null;

        Store emptyNameStore = new Store(id, name);

        // WHEN
        assertThrows(StoreNameCannotBeEmptyException.class, () -> {
            storeService.create(emptyNameStore);
        });

        // THEN
        verify(storeRepository, never()).create(emptyNameStore);
    }

    @Test
    void givenCorrectStore_whenCreated_savesStoreInRepository() throws Exception {
        // GIVEN
        Integer id = 1234;
        String name = "BHV";

        Store correctStore = new Store(null, name);
        Store savedStore = new Store(id, name);
        when(storeRepository.create(correctStore)).thenReturn(savedStore);

        // WHEN
        Store returnedStore = storeService.create(correctStore);

        // THEN
        verify(storeRepository, atLeastOnce()).create(correctStore);
        assertThat(returnedStore).isEqualTo(savedStore);
    }
    
    @Test
    void givenExistingStore_whenDeleted_thenNoException() throws ModelNotFoundException {
    	// GIVEN
        Integer id = 1234;
        
        doNothing().when(storeRepository).deleteById(id);
        
        // WHEN
        storeService.delete(id);

        // THEN
        verify(storeRepository, atLeastOnce()).deleteById(id);
    }
    
    @Test
    void givenNotExistingStore_whenDeleted_thenNoException() throws ModelNotFoundException {
    	// GIVEN
        Integer id = 1234;
        
        doThrow(new ModelNotFoundException()).when(storeRepository).deleteById(id);
        
        // WHEN
        // THEN
        assertThrows(ModelNotFoundException.class, () -> {
        	storeService.delete(id);
        });
        verify(storeRepository, atLeastOnce()).deleteById(id);
    }
}