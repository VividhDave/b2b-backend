package com.divergent.mahavikreta.service.impl;

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

import com.divergent.mahavikreta.entity.Attribute;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.AttributeRepository;
import com.divergent.mahavikreta.service.AttributeService;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.utility.AppUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AttributeServiceImpl implements AttributeService {

	@Autowired
	AttributeRepository attributeRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	LogService logService;

	@Override
	public Attribute save(Attribute attribute) {
		if (AppUtility.isEmpty(attribute)) {
			throw new GenricException("Invlid Attribute Object");
		}
		if (attributeRepository.existsByName(attribute.getName())) {
			throw new GenricException("Attribute Already Exist");
		}
		String key = attribute.getName().replaceAll("\\s+", "");
		attribute.setKey(key);
		return attributeRepository.save(attribute);
	}

	@Override
	public PageImpl<Attribute> getList(Pageable pageable, String attributeName) throws ParseException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();

		Root<Attribute> root = criteria.from(Attribute.class);

		List<Attribute> workEffortMaps = null;
		long count=0;
		Page<Attribute> page;
		if (AppUtility.isEmpty(attributeName)) {
			page = attributeRepository.findAllAttribute(pageable);
			workEffortMaps = (List<Attribute>) page.getContent();
			count = page.getTotalElements();

		} else {
			criteria.multiselect(root.get("id"), root.get("name"), root.get("status"));

			List<Predicate> predicates = setAdvanceSeachForAttribute(builder, root, attributeName);

			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);

			List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize()).getResultList();
			workEffortMaps = new ArrayList<>();
			try {
				for (Tuple tuple : workEffortTuples) {
					Attribute Attribute = new Attribute();
					Attribute.setId((Long) tuple.get(0));
					Attribute.setName((String) tuple.get(1));
					workEffortMaps.add(Attribute);
				}
				count = entityManager.createQuery(criteria).getResultList().size();
			} catch (Exception ex) {
				log.error(ex.getMessage());
				logService.saveErrorLog("Error getting Attribute list", "AttributeController", "getList",
						ex.getMessage());
			}
		}

		return new PageImpl<>(workEffortMaps, pageable, count);
	}

	private List<Predicate> setAdvanceSeachForAttribute(CriteriaBuilder builder, Root<Attribute> root,
			String attributeName) throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		if (!AppUtility.isEmpty(attributeName))
			predicates.add(builder.like(builder.lower(root.get("name")), "%" + attributeName.toLowerCase() + "%"));

		return predicates;
	}

	@Override
	public List<Attribute> getAllRecord() {
		return attributeRepository.findAll();
	}

	@Override
	public List<Attribute> findAttributeByCategoryId(Long id) {
		if (id == null) {
			throw new GenricException("Please enter valid Id");
		}
		return attributeRepository.findAttributeByCategoryId(id);
	}

}
