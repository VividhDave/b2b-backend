package com.divergent.mahavikreta.service.impl;

import com.divergent.mahavikreta.entity.*;
import com.divergent.mahavikreta.entity.filter.CardFilter;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.payload.AddToCartRequest;
import com.divergent.mahavikreta.repository.AddToCardRepository;
import com.divergent.mahavikreta.repository.ProductRepository;
import com.divergent.mahavikreta.repository.UserRepository;
import com.divergent.mahavikreta.service.AddToCardService;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.utility.AppUtility;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides {@link AddToCard} related operation.
 *
 * @author Aakash
 */
@Slf4j
@Service
public class AddToCardServiceImpl implements AddToCardService {

    @Autowired
    public AddToCardRepository addToCardRepository;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    LogService logService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddToCard addToCard(AddToCartRequest addToCart) {
        AddToCard addToCard = addToCardRepository.findAddToCardByProductIdAndUserIdAndStatus(addToCart.getProductId(),
                addToCart.getUserid(), true);
        if (AppUtility.isEmpty(addToCard)) {
            addToCard = new AddToCard();
            addToCard.setQty(addToCart.getQty());
            addToCard.setDeliveryCost(addToCart.getDeliveryCost());
            addToCard.setPrice(addToCart.getPrice());
            addToCard.setStatus(true);
            Product prod = productRepository.findProductById(addToCart.getProductId());
            if (AppUtility.isEmpty(prod)) {
                throw new GenricException("Product Not Exist");
            }
            addToCard.setProduct(prod);
            User user = userRepository.findUserById(addToCart.getUserid());
            if (AppUtility.isEmpty(user)) {
                throw new GenricException("User Not Exist");
            }
            addToCard.setUser(user);
        }
        return addToCardRepository.save(addToCard);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductFromCard(Long productId, Long userId) {
        addToCardRepository.deleteProductFromCard(productId, userId);
    }

    @Override
    public PageImpl<AddToCard> getCardDetailByUser(Pageable pageable, CardFilter cardFilter) throws ParseException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
        Root<AddToCard> root = criteria.from(AddToCard.class);
        Join<AddToCard, User> userJoin = root.join("user", JoinType.INNER);
        Join<AddToCard, Product> productJoin = root.join("product", JoinType.INNER);
        List<AddToCard> workEffortMaps = null;
        long count;
        Page<AddToCard> page;
        if (AppUtility.isEmpty(cardFilter)) {
            page = addToCardRepository.findAll(pageable);
            workEffortMaps = (List<AddToCard>) page.getContent();
            count = page.getTotalElements();
        } else {
            criteria.multiselect(root.get("id"), root.get("qty"), root.get("price"), root.get("deliveryCost"),
                    productJoin.get("id"), productJoin.get("productName"), productJoin.get("displayName"),
                    productJoin.get("description"), productJoin.get("discountPrice"), userJoin.get("id"),
                    userJoin.get("firstName"), userJoin.get("lastName"), root.get("status"));
            List<Predicate> predicates = setAdvanceSeachForCard(builder, root, cardFilter, productJoin, userJoin);
            criteria.where(predicates.toArray(new Predicate[]{}));
            List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
                    .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                    .setMaxResults(pageable.getPageSize()).getResultList();
            workEffortMaps = new ArrayList<>();
            try {
                for (Tuple tuple : workEffortTuples) {
                    AddToCard addToCard = new AddToCard();
                    addToCard.setId((Long) tuple.get(0));
                    addToCard.setQty((int) tuple.get(1));
                    addToCard.setPrice((double) tuple.get(2));
                    addToCard.setDeliveryCost((double) tuple.get(3));
                    addToCard.setStatus((boolean) tuple.get(12));
                    Product product = new Product();
                    product.setId((Long) tuple.get(4));
                    product.setProductName((String) tuple.get(5));
                    product.setDisplayName((String) tuple.get(6));
                    product.setDescription((String) tuple.get(7));
                    product.setDiscountPrice((double) tuple.get(8));
                    addToCard.setProduct(product);
                    User user = new User();
                    user.setId((Long) tuple.get(9));
                    user.setFirstName((String) tuple.get(10));
                    user.setLastName((String) tuple.get(11));
                    addToCard.setUser(user);
                    workEffortMaps.add(addToCard);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
                logService.saveErrorLog("Error getting Cart list", "AddToCartController", "getCartDetailByUser", ex.getMessage());
            }
            count = entityManager.createQuery(criteria).getResultList().size();
        }

        return new PageImpl<>(workEffortMaps, pageable, count);
    }

    private List<Predicate> setAdvanceSeachForCard(CriteriaBuilder builder, Root<AddToCard> root, CardFilter cardFilter,
                                                   Join<AddToCard, Product> productJoin, Join<AddToCard, User> userJoin) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        if (!AppUtility.isEmpty(cardFilter.getQty()) && cardFilter.getQty() > 0)
            predicates.add(builder.equal(root.get("qty"), cardFilter.getQty()));

        if (!AppUtility.isEmpty(cardFilter.getPrice()) && cardFilter.getPrice() > 0)
            predicates.add(builder.equal(root.get("price"), cardFilter.getPrice()));

        if (!AppUtility.isEmpty(cardFilter.getDeliveryCost()) && cardFilter.getDeliveryCost() > 0)
            predicates.add(builder.equal(root.get("deliveryCost"), cardFilter.getDeliveryCost()));

        if (!AppUtility.isEmpty(cardFilter.getProductId()) && cardFilter.getProductId() > 0)
            predicates.add(builder.equal(productJoin.get("id"), cardFilter.getProductId()));

        if (!AppUtility.isEmpty(cardFilter.getUserId()) && cardFilter.getUserId() > 0)
            predicates.add(builder.equal(userJoin.get("id"), cardFilter.getUserId()));

        if (cardFilter.getStatus())
            predicates.add(builder.equal(root.get("status"), cardFilter.getStatus()));

        return predicates;
    }

    @Override
    public List<AddToCard> findCartDetailsByUserId(Long userId) {
        return addToCardRepository.findAddToCardByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddToCard updateAddToCardProduct(AddToCartRequest addToCartRequest) {

        AddToCard addToCard = null;

        if (AppUtility.isEmpty(addToCartRequest)) {
            throw new GenricException("Empty or Null Data");

        } else {
            addToCard = addToCardRepository.findAddToCardByProductIdAndUserIdAndStatus(addToCartRequest.getProductId(),
                    addToCartRequest.getUserid(), true);

            if (AppUtility.isEmpty(addToCard)) {
                throw new GenricException("Data Not Found");

            } else {
                User user = userRepository.findUserById(addToCard.getUser().getId());
                Product product = productRepository.findProductById(addToCard.getProduct().getId());

                if (AppUtility.isEmpty(user) && AppUtility.isEmpty(product)) {
                    throw new GenricException("Product or User not Found");

                } else {

                    if (addToCartRequest.getDeliveryCost() != addToCard.getDeliveryCost()) {
                        addToCard.setDeliveryCost(addToCartRequest.getDeliveryCost());
                    }

                    if (addToCartRequest.getPrice() != addToCard.getPrice()) {
                        addToCard.setPrice(addToCartRequest.getPrice());
                    }

                    if (addToCartRequest.getQty() != addToCard.getQty()) {
                        addToCard.setQty(addToCartRequest.getQty());
                    }
                }

            }
        }

        return addToCardRepository.save(addToCard);
    }
}
