package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.AddToCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface provides {@link AddToCard} related Persistent Operation
 * 
 * @author Aakash
 * 
 * @see JpaRepository
 *
 */
@Repository
public interface AddToCardRepository extends JpaRepository<AddToCard, Long> {

	AddToCard findAddToCardByProductIdAndUserIdAndStatus(Long productId, Long userId, Boolean status);

	/**
	 * This method delete {@link AddToCard} by userId and productId
	 * 
	 * @param productId
	 * @param userId
	 */
	@Modifying
	@Query("delete from AddToCard where product.id=:productId and user.id=:userId")
	void deleteProductFromCard(@Param("productId") Long productId, @Param("userId") Long userId);

	/**
	 * This method delete {@link AddToCard} by userId
	 * 
	 * @param userId
	 */
	@Modifying
	@Query("delete from AddToCard where user.id=:userId")
	void deleteByUserId(@Param("userId") Long userId);

	/**
	 * This method finds {@link AddToCard} using product id, user id, and status
	 * 
	 * @param productId
	 * @param userId
	 * @param status
	 * @return
	 */
	@Query("select addToCard from AddToCard addToCard where addToCard.product.id in (:productId) and addToCard.user.id=:userId and addToCard.status=:status")
	List<AddToCard> getCardDetailByUser(@Param("productId") Long[] productId, @Param("userId") Long userId,
			@Param("status") Boolean status);

	/**
	 * This method finds all {@link AddToCard}
	 * 
	 * @param userId
	 * @return
	 */
	List<AddToCard> findAddToCardByUserId(Long userId);
}
