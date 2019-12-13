package com.scholanova.projectstore.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.models.Store;

@Repository
public class StockRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

    public StockRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
    
    
	public Stock create(Stock stockToCreate) {
		KeyHolder holder = new GeneratedKeyHolder();

        String query = "INSERT INTO STOCKS " +
                "(NAME , ID_STORE , TYPE , VALUE ) VALUES " +
                "(:name , :id_store , :type , :value)";

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", stockToCreate.getName())
                .addValue("id_store", stockToCreate.getId_store())
        		.addValue("type", stockToCreate.getType())
        		.addValue("value", stockToCreate.getValue());

        
        jdbcTemplate.update(query, parameters, holder);

        Integer newlyCreatedId = (Integer) holder.getKeys().get("ID");
        try {
            return this.getById(newlyCreatedId);
        } catch (ModelNotFoundException e) {
            return null;
        }
	}



	public Stock getById(Integer id) throws ModelNotFoundException {
		String query = "SELECT ID as id, " +
                "NAME AS name, " +
                "ID_STORE AS id_store, " +
                "TYPE AS type, " +
                "VALUE AS value " +
                "FROM STOCKS " +
                "WHERE ID = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        return jdbcTemplate.query(query,
                parameters,
                new BeanPropertyRowMapper<>(Stock.class))
                .stream()
                .findFirst()
                .orElseThrow(ModelNotFoundException::new);
        }



	public void deleteById(Integer id) throws ModelNotFoundException{
		
		String query = "DELETE " +
				"FROM STOCKS " +
                "WHERE ID = :id";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        
        int nbAffectedRow = jdbcTemplate.update(query, parameters);
        
        if ( nbAffectedRow == 0 ) 
        	throw new ModelNotFoundException();
		
		
	}

	
}
