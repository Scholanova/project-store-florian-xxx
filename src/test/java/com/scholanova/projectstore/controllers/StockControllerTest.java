package com.scholanova.projectstore.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.scholanova.projectstore.exceptions.StockNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.StockValueInvalidException;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.services.StockService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StockControllerTest {

	@LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();

    @MockBean
    private StockService stockService;

    @Captor
    ArgumentCaptor<Integer> storeIdCaptor;
    
    @Captor
    ArgumentCaptor<Stock> createStockArgumentCaptor;
    
    
    @Test
    void givenCreateStockURLCorrectBodyWithExistingStore_whenCalled_createsStore() throws Exception {
        // given
        String url = "http://localhost:{port}/stores/{id_store}/stocks";
        Integer Id_Store = 1;
        
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));
        urlVariables.put("id_store", String.valueOf(Id_Store));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = "{" +
                "\"id_store\":1," +
                "\"name\":\"Orange\"," +
                "\"type\":\"Fruit\"," +
                "\"value\":50" +
                "}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        Stock createdStock = new Stock(null, 1, "Orange", "Fruit", 50);
        when(stockService.create(storeIdCaptor.capture(), createStockArgumentCaptor.capture())).thenReturn(createdStock);

        // When
        ResponseEntity<Stock> responseEntity = template.exchange(url,
                HttpMethod.POST,
                httpEntity,
                Stock.class,
                urlVariables);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        
        Stock returnedStock = responseEntity.getBody();
        
        assertThat(returnedStock.getId_store()).isEqualTo(createdStock.getId_store());
        assertThat(returnedStock.getName()).isEqualTo(createdStock.getName());
        assertThat(returnedStock.getType()).isEqualTo(createdStock.getType());
        assertThat(returnedStock.getValue()).isEqualTo(createdStock.getValue());
    }
    
    @Test
    void givenCreateStockURLEmptyNameWithExistingStore_whenCalled_thenThrowStockNameCannotBeEmptyException() throws Exception {
        // given
        String url = "http://localhost:{port}/stores/{id_store}/stocks";
        Integer Id_Store = 1;
        
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));
        urlVariables.put("id_store", String.valueOf(Id_Store));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = "{" +
                "\"id_store\":1," +
                "\"name\":\"\"," +
                "\"type\":\"Fruit\"," +
                "\"value\":50" +
                "}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        Stock createdStock = new Stock(null, 1, "Orange", "Fruit", 50);
        when(stockService.create(storeIdCaptor.capture(), createStockArgumentCaptor.capture())).thenThrow(new StockNameCannotBeEmptyException());

        // When
        ResponseEntity<String> responseEntity = template.exchange(url,
                HttpMethod.POST,
                httpEntity,
                String.class,
                urlVariables);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("StockNameCannotBeEmptyException");
    }
    
    @Test
    void givenCreateStockURLWithValueFieldIsNotIntegerWithExistingStore_whenCalled_thenThrowStockValueInvalidException() throws Exception {
        // given
        String url = "http://localhost:{port}/stores/{id_store}/stocks";
        Integer Id_Store = 1;
        
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));
        urlVariables.put("id_store", String.valueOf(Id_Store));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = "{" +
                "\"id_store\":1," +
                "\"name\":\"Orange\"," +
                "\"type\":\"Fruit\"," +
                "\"value\":\"Orange\"" +
                "}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        when(stockService.create(storeIdCaptor.capture(), createStockArgumentCaptor.capture())).thenThrow(new StockValueInvalidException());

        // When
        ResponseEntity<String> responseEntity = template.exchange(url,
                HttpMethod.POST,
                httpEntity,
                String.class,
                urlVariables);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("StockValueInvalidException");
    }
    
    
    
	
}
