package com.bootcamp.dscatalog.resource;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.resources.ProductResource;
import com.bootcamp.dscatalog.services.ProductService;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import com.bootcamp.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService service;
	@Autowired
	private ObjectMapper objMapper;

	private Long noExistId;
	private Long existId;
	private Long dependentId;
	private PageImpl<ProductDTO> page;
	private ProductDTO productDTO;

	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		noExistId = 2L;
		dependentId = 3L;
		productDTO = Factory.createdProductDTO();
		page = new PageImpl<>(List.of(productDTO));

		when(service.findById(existId)).thenReturn(productDTO);
		when(service.findById(noExistId)).thenThrow(ResourceNotFoundException.class);
		when(service.findAllPaged((Pageable) ArgumentMatchers.any())).thenReturn(page);
		when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);
		when(service.update(ArgumentMatchers.eq(existId), ArgumentMatchers.any())).thenReturn(productDTO);
		when(service.update(ArgumentMatchers.eq(noExistId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
	
		doNothing().when(service).delete(existId);
		doThrow(ResourceNotFoundException.class).when(service).delete(noExistId);
		doThrow(DataBaseException.class).when(service).delete(dependentId);
	}
	@Test
	public void insertShouldReturnProductDTOWhenIdExists() throws Exception{
		String jsonBody = objMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(post("/products").content(jsonBody).contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}
	
	@Test
	public void deleterShouldReturnNotFoundWhenIdnoExists() throws Exception{
		mockMvc.perform(delete("/products/{id}", noExistId).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception{
		mockMvc.perform(delete("/products/{id}", existId).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExisted() throws Exception {
		String jsonBoby = objMapper.writeValueAsString(productDTO);

		mockMvc.perform(put("/products/{id}", existId).content(jsonBoby).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdNotExisted() throws Exception {
		String jsonBoby = objMapper.writeValueAsString(productDTO);

		mockMvc.perform(put("/products/{id}", noExistId).content(jsonBoby).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() throws Exception {
		mockMvc.perform(get("/products/{id}", existId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdNotExist() throws Exception {
		mockMvc.perform(get("/products/{id}", noExistId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void findAllPagedShouldReturnPaged() throws Exception {
		mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

}
