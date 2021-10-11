package com.bootcamp.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.repositories.ProductRepository;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existId;
	private Long noExistId;
	private Long totalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existId = 1L;
		noExistId = 1000L;
		totalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		service.delete(existId);
		
		Assertions.assertEquals(totalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(noExistId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPagedWhenPage0Size10() {
		PageRequest page = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(totalProducts, result.getTotalElements());	
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPagedWhenPageNoExists() {
		PageRequest page = PageRequest.of(50, 10);
		Page<ProductDTO> result = service.findAllPaged(page);
		
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnOrderPagedWhenSortByName() {
		PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = service.findAllPaged(page);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.toList().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.toList().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.toList().get(2).getName());
	}
}
