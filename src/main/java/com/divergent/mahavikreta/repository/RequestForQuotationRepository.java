package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.RequestForQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestForQuotationRepository extends JpaRepository<RequestForQuotation, Long> {

    @Query("select max((substring(rfq.rfqCode,10,20))) from RequestForQuotation rfq")
    String generateRfqCode();

    @Query("select requestForQuotation from RequestForQuotation requestForQuotation where requestForQuotation.product.id=:productId")
    List<RequestForQuotation> getQuotationByProduct(@Param("productId") Long productId);

    @Query("select quotation from RequestForQuotation quotation where quotation.product.id=:productID and quotation.user.id=:userId")
    RequestForQuotation searchByUserIdAndProductId(@Param("productID") Long pIdd,@Param("userId") Long uId);

}
