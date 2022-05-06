package com.divergent.mahavikreta.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

	Boolean existsByName(String attributeName);
	
	@Query("select attribute from Attribute attribute where attribute.name=?1 and attribute.category.id=?2")
	Attribute findAttributeByNameAndCategory(String name, Long id);
	
	@Query("select attribute from Attribute attribute where attribute.name=?1")
	Attribute findAttributeByName(String name);

	@Query("select attribute from Attribute attribute")
	Page<Attribute> findAllAttribute(Pageable pageable);
	
	@Query("select attr from Attribute attr where attr.category.id=?1")
	public List<Attribute> findAttributeByCategoryId(Long categoryId);

}
