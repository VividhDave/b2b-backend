package com.divergent.mahavikreta.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.entity.Category;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.CategoryRepository;
import com.divergent.mahavikreta.service.CategoryService;
import com.divergent.mahavikreta.utility.AppUtility;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Category save(Category category) {
		if (AppUtility.isEmpty(category)) {
			throw new GenricException("Invlid Category Object");
		}
		if (categoryRepository.existsByName(category.getName())) {
			throw new GenricException("Category Name Already Exist");
		}
		return categoryRepository.save(category);
	}
	
	@Override
	public List<Category> getAllRecord() {
		return categoryRepository.findAll();
	}

	@Override
	public PageImpl<Category> getList(Pageable pageable, String categoryName) throws ParseException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();

		Root<Category> root = criteria.from(Category.class);

		List<Category> workEffortMaps = null;
		long count;
		Page<Category> page;
		if (AppUtility.isEmpty(categoryName)) {
			page = categoryRepository.findAllCategory(pageable);
			workEffortMaps = (List<Category>) page.getContent();
			count = page.getTotalElements();

		} else {
			criteria.multiselect(root.get("id"), root.get("name"), root.get("status"));

			List<Predicate> predicates = setAdvanceSeachForCategory(builder, root, categoryName);

			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);

			List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize()).getResultList();
			workEffortMaps = new ArrayList<>();
			for (Tuple tuple : workEffortTuples) {
				Category Category = new Category();
				Category.setId((Long) tuple.get(0));
				Category.setName((String) tuple.get(1));
				Category.setStatus((Boolean) tuple.get(2));
				workEffortMaps.add(Category);
			}
			count = entityManager.createQuery(criteria).getResultList().size();
		}

		return new PageImpl<>(workEffortMaps, pageable, count);
	}

	private List<Predicate> setAdvanceSeachForCategory(CriteriaBuilder builder, Root<Category> root,
			String categoryName) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		if (!AppUtility.isEmpty(categoryName))
			predicates.add(builder.like(builder.lower(root.get("name")), "%" + categoryName.toLowerCase() + "%"));

		return predicates;
	}
	
	@Override
	public Category saveImage(MultipartFile file, Long categoryId) throws IOException {
		Category ct=categoryRepository.findCategoryById(categoryId);
		ct.setImage(file.getBytes());
		return categoryRepository.save(ct);
	}

	@Override
	public Category findCategoryById(Long id) {
		return categoryRepository.findCategoryById(id);
	}

	@Override
	public void deleteCategoryById(long id) {
		this.categoryRepository.deleteById(id);
	}


}
