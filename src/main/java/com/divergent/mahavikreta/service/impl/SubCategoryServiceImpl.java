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

import com.divergent.mahavikreta.entity.SubCategory;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.SubCategoryRepository;
import com.divergent.mahavikreta.service.SubCategoryService;
import com.divergent.mahavikreta.utility.AppUtility;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

	@Autowired
	SubCategoryRepository subCategoryRepository;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public SubCategory save(SubCategory subcategory) {
		try {
			if (AppUtility.isEmpty(subcategory)) {
				throw new GenricException("Invlid SubCategory Object");
			}
			if (!AppUtility.isEmpty(subCategoryRepository.findSubCategoryByName(subcategory.getName(),
					subcategory.getCategory().getId()))) {
				throw new GenricException("SubCategory Name Already Exist");
			}
			if(AppUtility.isEmpty(subcategory.getGst())){
				throw new GenricException("Please enter Gst Percentage");
			}
		} catch (Exception ex) {
			throw new GenricException(ex.getMessage());
		}
		return subCategoryRepository.save(subcategory);
	}

	@Override
	public PageImpl<SubCategory> getList(Pageable pageable, String subCategoryName) throws ParseException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();

		Root<SubCategory> root = criteria.from(SubCategory.class);

		List<SubCategory> workEffortMaps = null;
		long count;
		Page<SubCategory> page;
		if (AppUtility.isEmpty(subCategoryName)) {
			page = subCategoryRepository.findAllSubCategory(pageable);
			workEffortMaps = (List<SubCategory>) page.getContent();
			count = page.getTotalElements();

		} else {
			criteria.multiselect(root.get("id"), root.get("name"), root.get("status"));

			List<Predicate> predicates = setAdvanceSeachForCategory(builder, root, subCategoryName);

			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);

			List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize()).getResultList();
			workEffortMaps = new ArrayList<>();
			for (Tuple tuple : workEffortTuples) {
				SubCategory subCategory = new SubCategory();
				subCategory.setId((Long) tuple.get(0));
				subCategory.setName((String) tuple.get(1));
				subCategory.setStatus((Boolean) tuple.get(2));
				workEffortMaps.add(subCategory);
			}
			count = entityManager.createQuery(criteria).getResultList().size();
		}

		return new PageImpl<>(workEffortMaps, pageable, count);
	}
	
	private List<Predicate> setAdvanceSeachForCategory(CriteriaBuilder builder, Root<SubCategory> root,
			String subCategoryName) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		if (!AppUtility.isEmpty(subCategoryName))
			predicates.add(builder.like(builder.lower(root.get("name")), "%" + subCategoryName.toLowerCase() + "%"));

		return predicates;
	}


	@Override
	public List<SubCategory> getAllRecord() {
		return subCategoryRepository.findAll();
	}

	@Override
	public List<SubCategory> getByCategoryId(Long categoryId) {
		if(categoryId==null) {
			throw new GenricException("Please enter valid Id");
		}
		return subCategoryRepository.findSubCategoryByCategoryId(categoryId);
	}
	
	@Override
	public SubCategory saveImage(MultipartFile file, Long subCategoryId) throws IOException {
		SubCategory sct=subCategoryRepository.findSubCategoryById(subCategoryId);
		sct.setImage(file.getBytes());
		return subCategoryRepository.save(sct);
	}

	@Override
	public SubCategory findSubCategoryById(Long id) {
		return subCategoryRepository.findSubCategoryById(id);
	}

	@Override
	public void deleteSubCategoryById(long id) {
		this.subCategoryRepository.deleteById(id);

	}

}
