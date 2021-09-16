package com.bootcamp.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bootcamp.dscatalog.entities.Category;

@Repository
public interface CategorygRepository extends JpaRepository<Category, Long>{

	
}
