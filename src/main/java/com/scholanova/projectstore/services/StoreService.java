package com.scholanova.projectstore.services;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.StoreRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store get(Integer id) throws ModelNotFoundException {
    	return storeRepository.getById(id);
    }
    
    public Boolean delete(Integer id) {
    	int rows = storeRepository.deleteById(id);
    	return rows > 0;
    }
    
	public Boolean update(Store store) {
		int rows = storeRepository.update(store);
		return rows > 0;
	}
    
    public Store create(Store store) throws StoreNameCannotBeEmptyException {

        if (isNameMissing(store)) {
            throw new StoreNameCannotBeEmptyException();
        }

        return storeRepository.create(store);
    }  

    	
    private boolean isNameMissing(Store store) {
        return store.getName() == null ||
                store.getName().trim().length() == 0;
    }


}
