package com.divergent.mahavikreta.service.impl;

import java.text.ParseException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.entity.PromotionType;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.PromotionTypeRepository;
import com.divergent.mahavikreta.service.PromotionTypeService;
import com.divergent.mahavikreta.utility.AppUtility;

@Service
public class PromotionTypeServiceImpl implements PromotionTypeService {

	@Autowired
	PromotionTypeRepository promotionTypeRepository;

	@Override
	public PromotionType save(PromotionType promotionType) {
		PromotionType pr = null;
		try {
			if (AppUtility.isEmpty(promotionType)) {
				throw new GenricException("Please enter valid Promotion Type Object");
			}
			if (!AppUtility.isEmpty(
					promotionTypeRepository.findPromotionTypeByPromoTypeCode(promotionType.getPromoTypeCode()))) {
				throw new GenricException("Promotion Type Code Already exist");
			}
			pr = promotionTypeRepository.save(promotionType);
		} catch (Exception ex) {
			throw new GenricException(ex.getMessage());
		}
		return pr;
	}

	@Override
	public List<PromotionType> getAllRecord() {
		return promotionTypeRepository.findAll();
	}

	@Override
	public PageImpl<PromotionType> getList(Pageable pageable) throws ParseException {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
//
//		Root<Product> root = criteria.from(Product.class);

		List<PromotionType> workEffortMaps = null;
		long count;
		Page<PromotionType> page;
//		if (AppUtility.isEmpty(ProductName)) {
		page = promotionTypeRepository.findAllPromotionType(pageable);
		workEffortMaps = (List<PromotionType>) page.getContent();
		count = page.getTotalElements();

//		} else {
//			criteria.multiselect(root.get("id"), root.get("name"), root.get("status"));
//
//			List<Predicate> predicates = setAdvanceSeachForProduct(builder, root, ProductName);
//
//			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);
//
//			List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
//					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
//					.setMaxResults(pageable.getPageSize()).getResultList();
//			workEffortMaps = new ArrayList<>();
//			for (Tuple tuple : workEffortTuples) {
//				Product Product = new Product();
//				Product.setId((Long) tuple.get(0));
//				Product.setName((String) tuple.get(1));
//				Product.setStatus((Boolean) tuple.get(2));
//				workEffortMaps.add(Product);
//			}
//			count = entityManager.createQuery(criteria).getResultList().size();
//		}

		return new PageImpl<>(workEffortMaps, pageable, count);
	}

	@Override
	public PromotionType findById(Long id) {
		if (id == null) {
			throw new GenricException("Please enter valid Id");
		}
		return promotionTypeRepository.findPromotionTypeById(id);
	}

//	private List<Predicate> setAdvanceSeachForProduct(CriteriaBuilder builder, Root<Product> root,
//			String ProductName) throws ParseException {
//		List<Predicate> predicates = new ArrayList<>();
//		if (!AppUtility.isEmpty(ProductName))
//			predicates.add(builder.like(builder.lower(root.get("name")), "%" + productName.toLowerCase() + "%"));
//
//		return predicates;
//	}

}
