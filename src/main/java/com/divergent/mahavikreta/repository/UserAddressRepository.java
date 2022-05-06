package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @Modifying
    @Query("delete from UserAddress where id=:id and user.id=:userId")
    void deleteAddress(@Param("id") Long id, @Param("userId") Long userId);

    @Query("select userAddress from UserAddress userAddress where userAddress.user.id=:userId and userAddress.status=true")
    List<UserAddress> getAddress(@Param("userId") Long userId);
    
    @Query("select userAddress from UserAddress userAddress where userAddress.id=?1 and userAddress.status=true")
    UserAddress findUserAddressById(Long id);
    
    @Query("select userAddress from UserAddress userAddress where userAddress.id=?1 and userAddress.user.id=?2")
    UserAddress findUserAddressByIdAndUserId(Long id,Long userId);
    
    @Transactional
	@Modifying
	@Query("update UserAddress ua set ua.status=false where ua.id=?1 and ua.user.id=?2")
	int updateAddressStatus(Long id,Long userId);

    @Query("select userAddress from UserAddress userAddress where userAddress.phone=:mobileNumber and userAddress.status=true")
    List<UserAddress> validatePhoneNumber(@Param("mobileNumber") String mobileNumber);
}
