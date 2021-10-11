package com.bootcamp.dscatalog.tests;

import java.time.Instant;

import com.bootcamp.dscatalog.dto.ProductDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;

public class Factory {

	public static Product createdProduct() {
		Product product = new Product(1L, "IPHONE 13", "Trash phone", 10000.0, "https://img.com/img.png", Instant.parse("2020-07-13T20:50:07.12345Z"));
		product.getCategories().add(createdCategory());
		return product;
	}
	public static ProductDTO createdProductDTO() {
		Product product = createdProduct();
		return new ProductDTO(product, product.getCategories());
	}
	public static Category createdCategory() {
		return new Category(2L, "Electronics");
	}
}
