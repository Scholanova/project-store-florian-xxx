package com.scholanova.projectstore.services;

import org.springframework.stereotype.Service;

import com.scholanova.projectstore.exceptions.StockNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.StockValueInvalidException;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.StockRepository;


@Service
public class StockService {
	
    private StockRepository stockRepository;

	public Stock create(Integer idStore, Stock stock) throws StockNameCannotBeEmptyException, StockValueInvalidException{
		// TODO Auto-generated method stub
		return null;
	}


	
}
