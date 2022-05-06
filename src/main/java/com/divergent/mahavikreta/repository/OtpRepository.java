package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OtpRepository extends JpaRepository<OneTimePassword, Long> {

    OneTimePassword findByWhatsappMobileNumber(String whatsappMobileNumber);

    @Modifying
    @Query("delete from OneTimePassword where whatsappMobileNumber=:whatsappMobileNumber")
    void deleteByWhatsappMobileNumber(@Param("whatsappMobileNumber") String whatsappMobileNumber);

    @Query("update OneTimePassword o set o.oneTimePassword=: oneTimePassword where o.whatsappMobileNumber=: whatsappMobileNumber")
    void UpdateOTP(@Param("oneTimePassword")int oneTimePassword,@Param("whatsappMobileNumber") String whatsappMobileNumber);
}
