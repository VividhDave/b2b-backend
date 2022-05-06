package com.divergent.mahavikreta.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

// import com.divergent.mahavikreta.entity.CustomerUserPrivilege;
import com.divergent.mahavikreta.entity.OneTimePassword;
import com.divergent.mahavikreta.entity.Role;
import com.divergent.mahavikreta.entity.UserAddress;
import com.divergent.mahavikreta.entity.filter.AddressFilter;
import com.divergent.mahavikreta.payload.PasswordChange;
import com.razorpay.RazorpayException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.divergent.mahavikreta.entity.User;


public interface UserService extends UserDetailsService {

	Map<String, Object> getAdditionalInformation(OAuth2Authentication authentication);

	public User save(User user) throws RazorpayException;
	
	public User update(Long id, User user);
	
	public User findById(Long id);
	
	PageImpl<User> getList(Pageable pageable) throws ParseException;

	public List<User> findByUsername(String username);

	List<UserAddress> saveOrUpdateAddress(AddressFilter addressFilter);

	List<UserAddress> deleteAddress(Long id, Long userId);

	List<UserAddress> getAddress(Long userId);
	
	UserAddress findAddressById(Long id);

	User validatePhoneNumber(String mobileNumber);

	OneTimePassword verifyUserWhatsappNumber(String whatsappMobileNumber, String oneTimePassword);

	OneTimePassword sendOtp(String whatsappMobileNumber);

	public String saveUser(User user);

	List<User> listRole(String  userRole);

    void changePassword(PasswordChange passwordChange);
}
