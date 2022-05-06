package com.divergent.mahavikreta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.divergent.mahavikreta.entity.RazorpayVirtualAccount;

public interface RazorpayVirtualAccountRepository  extends JpaRepository<RazorpayVirtualAccount, Long> {
	
	
	RazorpayVirtualAccount findRazorpayVirtualAccountByUserId(Long Id);
	
	@Query("select acc from RazorpayVirtualAccount acc where acc.virtualAccountId=?1")
	RazorpayVirtualAccount findRazorpayVirtualAccountByAccountNumber(String virtualAccountId);

	@Query(nativeQuery = true, value = "select acc.* from rzpy_virtual_acc acc inner join users u on u.id=acc.user_id where u.rzpay_cust_id=?1")
	RazorpayVirtualAccount getRazrVirtualAcByCustId(String customerId);

}
