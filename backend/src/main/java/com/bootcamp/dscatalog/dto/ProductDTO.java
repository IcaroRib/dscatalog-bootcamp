package com.bootcamp.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Product;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank(message = "Campo Obrigatório")
	@Size(min = 5, max = 60, message = "Campo com minimo de 5 e maximo de 60 caracteres")
	private String name;
	@NotBlank(message = "Campo Obrigatório")
	private String description;
	@Positive(message = "O valor não pode ser negativo")
	private Double price;
	private String img_URL;
	@PastOrPresent(message = "A Data não pode ser futura")
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO() {
	}
	
	public ProductDTO(Long id, String name, String description, Double price, String img_URL, Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.img_URL = img_URL;
		this.date = date;
	}

	public ProductDTO(Product entity){
		id = entity.getId();
		name = entity.getName();
		description = entity.getDescription();
		price = entity.getPrice();
		img_URL = entity.getImg_URL();
		date = entity.getDate();
	}
	
	public ProductDTO(Product entity, Set<Category> categories){
		this(entity);
		categories.forEach(x -> this.categories.add(new CategoryDTO(x)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImg_URL() {
		return img_URL;
	}

	public void setImg_URL(String img_URL) {
		this.img_URL = img_URL;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}
}
