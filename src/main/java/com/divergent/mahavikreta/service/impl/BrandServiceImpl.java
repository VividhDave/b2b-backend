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

import com.divergent.mahavikreta.entity.Brand;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.BrandRepository;
import com.divergent.mahavikreta.service.BrandService;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.utility.AppUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	BrandRepository brandRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	LogService logService;

	@Override
	public Brand save(Brand brand) {
		if (AppUtility.isEmpty(brand)) {
			throw new GenricException("Invlid Brand Object");
		}
		if (brandRepository.existsByName(brand.getName())) {
			throw new GenricException("Brand Name Already Exist");
		}
		return brandRepository.save(brand);
	}

	@Override
	public List<Brand> getAllRecord() {
		return brandRepository.findAll();
	}

	@Override
	public PageImpl<Brand> getList(Pageable pageable, String brandName) throws ParseException {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();

		Root<Brand> root = criteria.from(Brand.class);

		List<Brand> workEffortMaps = null;
		long count=0;
		Page<Brand> page;
		if (AppUtility.isEmpty(brandName)) {
			page = brandRepository.findAllBrand(pageable);
			workEffortMaps = (List<Brand>) page.getContent();
			count = page.getTotalElements();
		} else {
			try {
				criteria.multiselect(root.get("id"), root.get("name"), root.get("status"));

				List<Predicate> predicates = setAdvanceSeachForBrand(builder, root, brandName);

				criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);

				List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
						.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
						.setMaxResults(pageable.getPageSize()).getResultList();
				workEffortMaps = new ArrayList<>();
				for (Tuple tuple : workEffortTuples) {
					Brand Brand = new Brand();
					Brand.setId((Long) tuple.get(0));
					Brand.setName((String) tuple.get(1));
					Brand.setStatus((Boolean) tuple.get(2));
					workEffortMaps.add(Brand);
				}
				count = entityManager.createQuery(criteria).getResultList().size();
			} catch (Exception ex) {
				log.error(ex.getMessage());
				logService.saveErrorLog("Error getting Brand list", "BrandController", "getList",
						ex.getMessage());
			}
		}

		return new PageImpl<>(workEffortMaps, pageable, count);
	}

	private List<Predicate> setAdvanceSeachForBrand(CriteriaBuilder builder, Root<Brand> root, String brandName)
			throws ParseException {
		List<Predicate> predicates = new ArrayList<>();
		if (!AppUtility.isEmpty(brandName))
			predicates.add(builder.like(builder.lower(root.get("name")), "%" + brandName.toLowerCase() + "%"));

		return predicates;
	}

	@Override
	public Brand saveImage(MultipartFile file, Long brandId) throws IOException {
		try {
		Brand br = brandRepository.findBrandById(brandId);
		br.setImage(file.getBytes());
		return brandRepository.save(br);
		}catch(Exception ex) {
			log.error(ex.getMessage());
			logService.saveErrorLog("Error savig brand image", "BrandController", "saveImage",
					ex.getMessage());
			throw new GenricException("Image not save due to error"+ ex.getMessage());
		}
	}

	@Override
	public Brand findBrandById(Long id) {
		return brandRepository.findBrandById(id);
	}

}
