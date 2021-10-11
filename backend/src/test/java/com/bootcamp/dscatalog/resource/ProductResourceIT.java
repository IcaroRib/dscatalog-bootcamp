package com.bootcamp.dscatalog.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objMapper;
	
	private Long noExistId;
	private Long existId;
	private Long countProduct;
	
	@BeforeEach
	void setUp() throws Exception {
		existId = 1L;
		noExistId = 1000L;
		countProduct = 25L;
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortedName() throws Exception{
		mockMvc.perform(get("/products?page=0&size=10&sort=name,asc").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(countProduct))
		.andExpect(jsonPath("$.content").exists()).andExpect(jsonPath("$.content[0].name").value("Macbook Pro"))
		.andExpect(jsonPath("$.content[1].name").value("PC Gamer")).andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExisted() throws Exception {
		ProductDTO productDTO = Factory.createdProductDTO();
		String jsonBoby = objMapper.writeValueAsString(productDTO);
		String expectedName = productDTO.getName();
		String expectedDescription = productDTO.getDescription();
		
		
		mockMvc.perform(put("/products/{id}", existId).content(jsonBoby).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(existId))
		        .andExpect(jsonPath("$.name").value(expectedName)).andExpect(jsonPath("$.description").value(expectedDescription));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdNotExisted() throws Exception {
		ProductDTO productDTO = Factory.createdProductDTO();
		String jsonBoby = objMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(put("/products/{id}", noExistId).content(jsonBoby).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}
}
