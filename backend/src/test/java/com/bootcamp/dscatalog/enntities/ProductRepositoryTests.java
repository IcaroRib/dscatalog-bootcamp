package com.bootcamp.dscatalog.enntities;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.bootcamp.dscatalog.entities.Product;
import com.bootcamp.dscatalog.repositories.ProductRepository;
import com.bootcamp.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private Long existsId;
	private Long notExistsId;
	private Integer countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existsId = 1L;
		notExistsId = 2000L;
		countTotalProducts = 25;
	}

	@Test
	public void findByIdShouldReturnOptionalProductWhenIdExists() {
		Optional<Product> p = repository.findById(existsId);
		Assertions.assertTrue(p.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalProductWhenIdNotExists() {
		Optional<Product> p = repository.findById(notExistsId);
		Assertions.assertTrue(p.isEmpty());
	}
	
	@Test
	public void insertShouldAutoIncrementIdWhenIdIsNull() {
		Product p = Factory.createdProduct();
		p.setId(null);
		Product product = repository.save(p);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(product.getId(), countTotalProducts + 1);
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		repository.deleteById(existsId);
		Optional<Product> p = repository.findById(existsId);
		Assertions.assertFalse(p.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdNotExists() {	
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(notExistsId);
		});
	}
}
