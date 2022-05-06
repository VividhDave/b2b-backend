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

import com.divergent.mahavikreta.entity.ChilledCategory;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.ChilledCategoryRepository;
import com.divergent.mahavikreta.service.ChilledCategoryService;
import com.divergent.mahavikreta.utility.AppUtility;

@Service
public class ChilledCategoryServiceImpl implements ChilledCategoryService {

	@Autowired
	ChilledCategoryRepository chilledCategoryRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public ChilledCategory save(ChilledCategory chilledCategory) {
		try {
			if (AppUtility.isEmpty(chilledCategory)) {
				throw new GenricException("Invlid ChilledCategory Object");
			}
			if (!AppUtility.isEmpty(chilledCategoryRepository.findChilledCategoryByNameAndSubCatId(chilledCategory.getName(),
					chilledCategory.getSubCategory().getId()))) {
				throw new GenricException("Chilled Category Name Already Exist");
			}
		} catch (Exception ex) {
			throw new GenricException(ex.getMessage());
		}
		return chilledCategoryRepository.save(chilledCategory);
	}

	@Override
	public PageImpl<ChilledCategory> getList(Pageable pageable, String chilledCategoryName) throws ParseException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();

		Root<ChilledCategory> root = criteria.from(ChilledCategory.class);

		List<ChilledCategory> workEffortMaps = null;
		long count;
		Page<ChilledCategory> page;
		if (AppUtility.isEmpty(chilledCategoryName)) {
			page = chilledCategoryRepository.findAllChilledCategory(pageable);
			workEffortMaps = (List<ChilledCategory>) page.getContent();
			count = page.getTotalElements();

		} else {
			criteria.multiselect(root.get("id"), root.get("name"), root.get("status"));

			List<Predicate> predicates = setAdvanceSeachForCategory(builder, root, chilledCategoryName);

			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);

			List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize()).getResultList();
			workEffortMaps = new ArrayList<>();
			for (Tuple tuple : workEffortTuples) {
				ChilledCategory chilledCategory = new ChilledCategory();
				chilledCategory.setId((Long) tuple.get(0));
				chilledCategory.setName((String) tuple.get(1));
				chilledCategory.setStatus((Boolean) tuple.get(2));
				workEffortMaps.add(chilledCategory);
			}
			count = entityManager.createQuery(criteria).getResultList().size();
		}

		return new PageImpl<>(workEffortMaps, pageable, count);
	}
	
	private List<Predicate> setAdvanceSeachForCategory(CriteriaBuilder builder, Root<ChilledCategory> root,
			String chilledCategoryName) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		if (!AppUtility.isEmpty(chilledCategoryName))
			predicates.add(builder.like(builder.lower(root.get("name")), "%" + chilledCategoryName.toLowerCase() + "%"));

		return predicates;
	}

	@Override
	public List<ChilledCategory> getAllRecord() {
		return chilledCategoryRepository.findAll();
	}

	@Override
	public List<ChilledCategory> getBySubCategoryId(Long id) {
		if(id==null) {
			throw new GenricException("Please enter valid Id");
		}
		return chilledCategoryRepository.findChilledCategoryBySubCategoryId(id);
	}

	@Override
	public ChilledCategory saveImage(MultipartFile file, Long chilledCategoryId) throws IOException {
		ChilledCategory ct=chilledCategoryRepository.findChilledCategoryById(chilledCategoryId);
		ct.setImage(file.getBytes());
		return chilledCategoryRepository.save(ct);
	}

	@Override
	public ChilledCategory findChilledCategoryById(Long id) {
		return chilledCategoryRepository.findChilledCategoryById(id);
	}

}
