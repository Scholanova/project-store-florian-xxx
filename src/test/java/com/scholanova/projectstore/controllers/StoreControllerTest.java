package com.scholanova.projectstore.controllers;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.services.StoreService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StoreControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();

    @MockBean
    private StoreService storeService;

    @Captor
    ArgumentCaptor<Store> createStoreArgumentCaptor;
    
    @Captor
    ArgumentCaptor<Integer> getStoreArgumentCaptor;
    
    @Captor
    ArgumentCaptor<Integer> deleteStoreArgumentCaptor;

    @Test
    void givenCreateStoreURLCorrectBody_whenCalled_createsStore() throws Exception {
        // given
        String url = "http://localhost:{port}/stores";

        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = "{" +
                "\"name\":\"Boulangerie\"" +
                "}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        Store createdStore = new Store(123, "Boulangerie");
        when(storeService.create(createStoreArgumentCaptor.capture())).thenReturn(createdStore);

        // When
        ResponseEntity responseEntity = template.exchange(url,
                HttpMethod.POST,
                httpEntity,
                String.class,
                urlVariables);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isEqualTo(
                "{" +
                        "\"id\":123," +
                        "\"name\":\"Boulangerie\"" +
                        "}"
        );
        
        Store storeToCreate = createStoreArgumentCaptor.getValue();
        assertThat(storeToCreate.getName()).isEqualTo("Boulangerie");
    }
    
    @Test
    void givenCreateStoreURLWrongBody_whenCalled_notCreateStore() throws Exception {
    	// given
        String url = "http://localhost:{port}/stores";

        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = "{}";
        HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

        when(storeService.create(createStoreArgumentCaptor.capture())).thenThrow(new StoreNameCannotBeEmptyException());
   
        // When
        ResponseEntity responseEntity = template.exchange(url,
                HttpMethod.POST,
                httpEntity,
                String.class,
                urlVariables);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("StoreNameCannotBeEmptyException");
        
        Store storeToCreate = createStoreArgumentCaptor.getValue();
        assertThat(storeToCreate.getName()).isEqualTo(null);
    }
    
    @Test
    void givenGetStoreURLWithExistingId_whenCalled_thenReturnExistingStore() throws Exception {
    	// GIVEN
        String url = "http://localhost:{port}/stores/{id}";
        int id = 1234;
        String name = "BEC";
        
        Store existingStore = new Store(id, name);

        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));
        urlVariables.put("id", String.valueOf(id));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        
        when(storeService.get(getStoreArgumentCaptor.capture())).thenReturn(existingStore);
          
        // When
        ResponseEntity<Store> responseEntity = template.exchange(url,
                HttpMethod.GET,
                httpEntity,
                Store.class,
                urlVariables);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        
        assertThat(getStoreArgumentCaptor.getValue()).isEqualTo(id);
               
        Store responseStore = responseEntity.getBody();
        assertThat(responseStore.getId()).isEqualTo(existingStore.getId());
        assertThat(responseStore.getName()).isEqualTo(existingStore.getName());
    }
    
    @Test
    void givenGetStoreURLWithNotExistingId_whenCalled_thenReturnExistingStore() throws Exception {
    	// GIVEN
        String url = "http://localhost:{port}/stores/{id}";
        int id = 1234;
        String name = "BEC";
        
        Store existingStore = new Store(id, name);

        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));
        urlVariables.put("id", String.valueOf(id));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        
        when(storeService.get(getStoreArgumentCaptor.capture())).thenThrow(new ModelNotFoundException());
        
        // When
        ResponseEntity<String> responseEntity = template.exchange(url,
                HttpMethod.GET,
                httpEntity,
                String.class,
                urlVariables);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(getStoreArgumentCaptor.getValue()).isEqualTo(id);
        assertThat(responseEntity.getBody()).isEqualTo("ModelNotFoundException");    
    }
    
    @Test
    void givenDeleteStoreURLWithExistingId_whenCalled_thenReturnNothing() throws Exception {
    	// GIVEN
        String url = "http://localhost:{port}/stores/{id}";
        int id = 1234;
        
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));
        urlVariables.put("id", String.valueOf(id));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        
        doNothing().when(storeService).delete(deleteStoreArgumentCaptor.capture());
        
        // WHEN
        ResponseEntity<Void> responseEntity = template.exchange(url,
        		HttpMethod.DELETE,
        		httpEntity,
        		Void.class,
        		urlVariables);
        
        // THEN
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(deleteStoreArgumentCaptor.getValue()).isEqualTo(id);
    }
    
    @Test
    void givenDeleteStoreURLWithNotExistingId_whenCalled_thenThrowException() throws Exception {
    	// GIVEN
        String url = "http://localhost:{port}/stores/{id}";
        int id = 1234;
        
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("port", String.valueOf(port));
        urlVariables.put("id", String.valueOf(id));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        
        doThrow(new ModelNotFoundException()).when(storeService).delete(deleteStoreArgumentCaptor.capture());
        
        // WHEN
        ResponseEntity<String> responseEntity = template.exchange(url,
        		HttpMethod.DELETE,
        		httpEntity,
        		String.class,
        		urlVariables);
        
        // THEN
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(deleteStoreArgumentCaptor.getValue()).isEqualTo(id);
        assertThat(responseEntity.getBody()).isEqualTo("ModelNotFoundException");
    }
    
}