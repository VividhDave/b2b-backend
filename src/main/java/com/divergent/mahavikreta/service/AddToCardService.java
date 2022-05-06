package com.divergent.mahavikreta.service;

import com.divergent.mahavikreta.entity.AddToCard;
import com.divergent.mahavikreta.entity.filter.CardFilter;
import com.divergent.mahavikreta.payload.AddToCartRequest;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.List;

public interface AddToCardService {

    AddToCard addToCard(AddToCartRequest addToCart);

    void deleteProductFromCard(Long productId, Long userId);

    PageImpl<AddToCard> getCardDetailByUser(Pageable pageable, CardFilter cardFilter) throws ParseException;
    
    List<AddToCard> findCartDetailsByUserId(Long userId);

    AddToCard updateAddToCardProduct(AddToCartRequest addToCart);
}
