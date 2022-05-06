package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.filter.PaymentFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.divergent.mahavikreta.entity.Payment;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
    @Query("select payment from Payment payment")
    Page<Payment> findAllPayment(Pageable pageable);

    @Query(nativeQuery = true, value = "select pd.id,pd.created_by,pd.created_date,pd.last_modified_date,pd.last_modified_by,pd.account_id" +
            ",pd.amount,pd.amount_refunded,pd.amount_transferred,pd.bank,pd.base_amount,pd.captured,pd.card_id,pd.contact,pd.currency" +
            ",pd.description,pd.email,pd.entity,pd.error_code,pd.error_description,pd.error_reason,pd.error_source,pd.error_step,pd.event" +
            ",pd.fee,pd.international,pd.rzpay_invoice_id,pd.method,pd.rzpay_order_id,pd.refund_payment_id,pd.refund_status,pd.status," +
            "pd.tax,pd.transaction_id,pd.user_id,pd.vpa,pd.wallet,pd.customer_id,CONCAT(u.first_name,' ',u.last_name)user_name, " +
            "acc.virtual_account_id from payment_details pd inner join users u on u.rzpay_cust_id=pd.customer_id inner" +
            " join rzpy_virtual_acc acc on acc.user_id=u.id limit :pageSize offset :pageNo")
    List<Map<String, String>> getAllPaymentHistory(@Param("pageSize") int pageSize,@Param("pageNo") int pageNo);

    @Query(nativeQuery = true, value = "select count(pd.id) from payment_details pd inner join users u on u.rzpay_cust_id=pd.customer_id inner" +
            " join rzpy_virtual_acc acc on acc.user_id=u.id")
    Long getCountPaymentHistory();

    @Query("select payment from Payment payment where payment.email like :email%")
    List<Payment> getPaymentListByEmail(@Param("email") String email);
}