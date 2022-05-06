package com.divergent.mahavikreta.repository;

import com.divergent.mahavikreta.entity.Role;
import com.divergent.mahavikreta.entity.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findByEmail(String email);

    User findUserById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("select user from User user Order by user.id DESC")
    Page<User> findAllUser(Pageable pageable);

    @Query("select user from User user where user.username like :username%")
    List<User> findByUsernameForSearch(@Param("username") String username);

    Boolean existsByEmailAndIdNot(String email, Long id);

    @Query(value = "select * from users where last_modified_date > timestamp(date_sub(now(), interval 1 minute)) " +
            "and enabled=false and id=:userId", nativeQuery = true)
    User validateUserForWhatsappNumber(@Param("userId") Long userId);

    @Query(value = "select * from users where whatsapp_mobile_number=:whatsappMobileNumber", nativeQuery = true)
    User checkMobileNumber(@Param("whatsappMobileNumber") String whatsappMobileNumber);

    Boolean existsByWhatsappMobileNumber(String whatsappMobileNumber);

    Boolean existsByGstin(String gstin);

    @Query("Select user from User user left join user.roles ur where ur.name IN(:role)")
    List<User> findByRole(@Param("role") String role);


}
