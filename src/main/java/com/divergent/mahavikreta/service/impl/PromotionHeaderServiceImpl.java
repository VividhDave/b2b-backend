package com.divergent.mahavikreta.service.impl;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.divergent.mahavikreta.entity.PromotionType;
import com.divergent.mahavikreta.entity.filter.PromotionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.divergent.mahavikreta.entity.PromotionHeader;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.PromotionHeaderRepository;
import com.divergent.mahavikreta.repository.PromotionTypeRepository;
import com.divergent.mahavikreta.service.PromotionHeaderService;
import com.divergent.mahavikreta.utility.AppUtility;

@Service
public class PromotionHeaderServiceImpl implements PromotionHeaderService {

    @Autowired
    PromotionHeaderRepository promotionHeaderRepository;
    
    @Autowired
    PromotionTypeRepository promotionTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PromotionHeader save(PromotionFilter promotion) {
        PromotionHeader pr = new PromotionHeader();
        try {
            PromotionType promotionType = promotionTypeRepository.findPromotionTypeById(promotion.getId());
            if(AppUtility.isEmpty(promotionType)) {
            	throw new GenricException("Please enter valid promotion Id");
            }
            pr.setPromotionType(promotionType);
            pr.setBuyerPurchases(promotion.getBuyerPurchases());
            pr.setBuyerGetsValue(promotion.getBuyerGetsValue());
            pr.setProductSelectionId(promotion.getProductSelectionId());
            pr.setBuyerGets(promotion.getBuyerGets());
            pr.setBuyerGetsValue(promotion.getBuyerGetsValue());
            pr.setAppliesTo(promotion.getAppliesTo());
            pr.setStartDate(promotion.getStartDate());
            pr.setEndDate(promotion.getEndDate());
            pr.setInternalDescription(promotion.getInternalDescription());
            pr.setTrackingId(promotion.getTrackingId());
            pr.setClaimCodeType(promotionType.getPromoType());
            pr.setPerCustomer(promotion.getRedemptionPerCustomer());
            pr.setClaimCode(promotion.getClaimCode());
            pr.setClaimCodeCombinability(promotion.getClaimCodeCombinability());
            pr.setCheckoutDisplayText(promotion.getCheckoutDisplayText());
            pr.setShortDisplayText(promotion.getShortDisplayName());
            pr.setDisplayDescription(promotion.getDisplayDescription());
            promotionHeaderRepository.save(pr);
        } catch (Exception ex) {
            throw new GenricException(ex.getMessage());
        }
        return pr;
    }

    @Override
    public List<PromotionHeader> getAllRecord() {
        return promotionHeaderRepository.findAll();
    }

    @Override
    public PageImpl<PromotionHeader> getList(Pageable pageable) throws ParseException {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
//
//		Root<Product> root = criteria.from(Product.class);

        List<PromotionHeader> workEffortMaps = null;
        long count;
        Page<PromotionHeader> page;
//		if (AppUtility.isEmpty(ProductName)) {
        page = promotionHeaderRepository.findAllPromotionHeader(pageable);
        workEffortMaps = (List<PromotionHeader>) page.getContent();
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
    public PromotionHeader findById(Long id) {
        if (id == null) {
            throw new GenricException("Please enter valid Id");
        }
        return promotionHeaderRepository.findPromotionHeaderById(id);
    }

//	private List<Predicate> setAdvanceSeachForProduct(CriteriaBuilder builder, Root<Product> root,
//			String ProductName) throws ParseException {
//		List<Predicate> predicates = new ArrayList<>();
//		if (!AppUtility.isEmpty(ProductName))
//			predicates.add(builder.like(builder.lower(root.get("name")), "%" + productName.toLowerCase() + "%"));
//
//		return predicates;
//	}

    @Override
    public PromotionHeader checkClaimCode(String code) {
        return promotionHeaderRepository.findPromotionHeaderByClaimCode(code)
                .orElseThrow(() -> new GenricException("Not Found"));
    }

}
