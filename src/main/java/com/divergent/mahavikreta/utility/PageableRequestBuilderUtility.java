package com.divergent.mahavikreta.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.divergent.mahavikreta.exception.GenricException;

/**
 * This class Provide Pageable request builder utility
 * 
 * @author Aakash
 *
 */
public class PageableRequestBuilderUtility {

	private PageableRequestBuilderUtility() {

	}

	/**
	 * 
	 * Replace by given map Sort Column To Entity Name Wise
	 * 
	 * @param entityNameMap {@link Map}
	 * @param pageable {@link Pageable}
	 * @return of pageRequestObject {@link Pageable}
	 */
	public static Pageable replaceSortFieldToEntityField(Map<String, String> entityNameMap, Pageable pageable) {

		Sort sort = pageable.getSort();
		if (sort.isUnsorted()) {
			return pageable;
		}
		Iterator<Order> orderIt = sort.iterator();
		if (!orderIt.hasNext()) {
			return pageable;
		}

		List<Order> entityOrders = new ArrayList<>();

		while (orderIt.hasNext()) {
			Order order = orderIt.next();
			String propety = order.getProperty();
			String entityPropety = entityNameMap.get(propety);
			if (Objects.isNull(entityPropety)) {
				throw new GenricException(String.format("%s sort column not exist", propety));
			}

			entityOrders.add(new Order(order.getDirection(), entityPropety));
		}
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(entityOrders));
	}
}
