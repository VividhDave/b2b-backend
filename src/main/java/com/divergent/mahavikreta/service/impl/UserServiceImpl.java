package com.divergent.mahavikreta.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.divergent.mahavikreta.entity.*;
import com.divergent.mahavikreta.entity.filter.AddressFilter;
import com.divergent.mahavikreta.payload.PasswordChange;
import com.divergent.mahavikreta.repository.*;
import com.divergent.mahavikreta.service.RazorpayVirtualAccountService;
import com.divergent.mahavikreta.utility.SmtpMailRest;
import com.divergent.mahavikreta.utility.YellowMessengerAPIIntegration;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.exception.UserRequestException;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.UserService;
import com.divergent.mahavikreta.utility.AppUtility;

import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserAddressRepository userAddressRepository;

    @Autowired
    OtpRepository otpRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    LogService logService;

    @Autowired
    RazorpayVirtualAccountService razorpayVirtualAccountService;

    @Autowired
    SmtpMailRest smtpMailRest;

    @Value("${app.toEmail}")
    String toEmails;

    @Override
    public User save(User user) throws RazorpayException {

        if (user.getId() == null) {
            if (user.getGstin() == null && AppUtility.isEmpty(user.getRoles())) {
                throw new UserRequestException(MsgConstants.ERROR_INVALID_GST_NO);
            }
//            if (!AppUtility.isEmail((user.getEmail()))) {
//                throw new UserRequestException("Please enter your email");
//            }
            if (AppUtility.isEmpty((user.getUsername()))) {
                throw new UserRequestException("Please enter your name");
            }
            Set<Role> roles = new HashSet<>();
            if (AppUtility.isEmpty(user.getRoles())) {
                roles.add(roleRepository.findByName(AppConstants.ROLE_USER));
                user.setRoles(roles);
            }
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new GenricException(MsgConstants.ERROR_USERNAME_ALREADY_EXIST);
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new GenricException(MsgConstants.ERROR_EMAIL_ALREADY_EXIST);
            }

            if (userRepository.existsByWhatsappMobileNumber(user.getWhatsappMobileNumber())) {
                throw new GenricException(MsgConstants.ERROR_PHONE_ALREADY_EXIST);
            }

            if (userRepository.existsByGstin(user.getGstin())) {
                throw new GenricException(MsgConstants.ERROR_GST_ALREADY_EXIST);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            User u = userRepository.findUserById(user.getId());
            user.setPassword(u.getPassword());
            user.setRoles(u.getRoles());
        }
        User u = razorpayVirtualAccountService.createRazorpayCustomer(user);

        try {
            razorpayVirtualAccountService.createVirtualAccount(user.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!AppUtility.isEmpty(u)) {
            sendMail(user);
            log.info("mail send");
        }
        return u;
    }

    @Override
    public User update(Long id, User user) {
        User u = new User();
        if (id == null || AppUtility.isEmpty(user)) {
            throw new GenricException("Please enter valid id");
        } else {
            Optional<User> userDetail = userRepository.findById(id);
            if (userDetail.isPresent()) {
                u.setId(id);
                u.setCreatedBy(userDetail.get().getCreatedBy());
                u.setCreatedDate(userDetail.get().getCreatedDate());
                u.setAccountNonExpired(true);
                u.setAccountNonLocked(true);
                u.setCredentialsNonExpired(true);
                u.setCustomerId(userDetail.get().getCustomerId());
                u.setEnabled(true);
                u.setUsername(userDetail.get().getUsername());
                u.setPassword(userDetail.get().getPassword());
                u.setLicenseNumber(userDetail.get().getLicenseNumber());
                if (user.getFirstName() != null && user.getFirstName().length() != 0 && !user.getFirstName().equals("")) {
                    if (!user.getFirstName().equalsIgnoreCase(userDetail.get().getFirstName())) {
                        u.setFirstName(user.getFirstName());
                    } else {
                        u.setFirstName(userDetail.get().getFirstName());
                    }
                } else {
                    throw new GenricException("Please Enter Your First Name");
                }
                if (user.getLastName() != null && user.getLastName().length() != 0 && !user.getLastName().equals("")) {
                    if (!user.getLastName().equalsIgnoreCase(userDetail.get().getLastName())) {
                        u.setLastName(user.getLastName());
                    } else {
                        u.setLastName(userDetail.get().getLastName());
                    }
                } else {
                    throw new GenricException("Please Enter Your Last Name");
                }
                if (user.getEmail() != null && user.getEmail().length() != 0 && !user.getEmail().equals("")) {
                    if (!AppUtility.isEmail((user.getEmail()))) {
                        throw new UserRequestException("Please Enter Valid Email Address");
                    } else {
                        if (userRepository.existsByEmailAndIdNot(user.getEmail(), id)) {
                            throw new GenricException("Email Address is already Register with Different User");
                        } else {
                            if (!user.getEmail().equalsIgnoreCase(userDetail.get().getEmail())) {
                                u.setEmail(user.getEmail());
                            } else {
                                u.setEmail(userDetail.get().getEmail());
                            }
                        }
                    }
                } else {
                    throw new GenricException("Please Enter Your Email Address");
                }
                if (user.getShopName() != null && user.getShopName().length() != 0 && !user.getShopName().equals("")) {
                    if (!user.getShopName().equalsIgnoreCase(userDetail.get().getShopName())) {
                        u.setShopName(user.getShopName());
                    } else {
                        u.setShopName(userDetail.get().getShopName());
                    }
                } else {
                    throw new GenricException("Please Enter Shop Name");
                }
                if (user.getGstin() != null && user.getGstin().length() != 0 && !user.getGstin().equals("")) {
                    if (!user.getGstin().equalsIgnoreCase(userDetail.get().getGstin())) {
                        u.setGstin(user.getGstin());
                    } else {
                        u.setGstin(userDetail.get().getGstin());
                    }
                } else {
                    throw new GenricException("Please Enter GST Number");
                }
                try {
                    userRepository.save(u);
                } catch (Exception e) {
                    throw new GenricException("Exception in user detail");
                }
            } else {
                throw new GenricException("User Not Found");
            }
        }
        return u;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public Map<String, Object> getAdditionalInformation(OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(AppConstants.USERNAME, authentication.getName());
        User user = userRepository.findByUsername(authentication.getName());
        additionalInfo.put("name", user.getFirstName());
        List<String> roles = new ArrayList<>();
        Set<Privileges> privilegesMap = new HashSet<>();
        for (GrantedAuthority role : authentication.getAuthorities()) {
            roles.add(role.getAuthority());
            privilegesMap.addAll(roleRepository.findByName(role.getAuthority()).getPrivileges());
        }
        additionalInfo.put("role", roles);
        additionalInfo.put("privileges", privilegesMap);
        return additionalInfo;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public PageImpl<User> getList(Pageable pageable) throws ParseException {
//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//
//		CriteriaQuery<Tuple> criteria = builder.createTupleQuery();
//
//		Root<User> root = criteria.from(User.class);

        List<User> workEffortMaps = null;
        long count;
        Page<User> page;
//		if (AppUtility.isEmpty(userName)) {
        page = userRepository.findAllUser(pageable);
        workEffortMaps = (List<User>) page.getContent();
        count = page.getTotalElements();

//		} else {
//			criteria.multiselect(root.get("id"), root.get("username"),root.get("email"),root.get("phone"),
//					root.get("firstName"),root.get("lastName"),
//					root.get("gstin"),root.get("enabled"),
//					root.get("address"),root.get("city"),
//					root.get("state"),root.get("pincode"),root.get("shopName"));
//
//			List<Predicate> predicates = setAdvanceSeachForUser(builder, root, userName);
//
//			criteria.where(predicates.toArray(new Predicate[] {})).distinct(true);
//
//			List<Tuple> workEffortTuples = entityManager.createQuery(criteria)
//					.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
//					.setMaxResults(pageable.getPageSize()).getResultList();
//			workEffortMaps = new ArrayList<>();
//			for (Tuple tuple : workEffortTuples) {
//				User user = new User();
//				user.setId((Long) tuple.get(0));
//				user.setUsername((String) tuple.get(1));
//				user.setEmail((String) tuple.get(2));
//				user.setPhone((String) tuple.get(3));
//				user.setFirstName((String) tuple.get(4));
//				user.setLastName((String) tuple.get(5));
//				user.setGstin((String) tuple.get(6));
//				user.setEnabled((Boolean) tuple.get(7));
//				user.setAddress((String) tuple.get(8));
//				user.setCity((String) tuple.get(9));
//				user.setState((String) tuple.get(10));
//				user.setPincode((String) tuple.get(11));
//				user.setShopName((String) tuple.get(12));
//				workEffortMaps.add(user);
//			}
//			count = entityManager.createQuery(criteria).getResultList().size();
//		}

        return new PageImpl<>(workEffortMaps, pageable, count);
    }

//	private List<Predicate> setAdvanceSeachForUser(CriteriaBuilder builder, Root<User> root,
//			String userName) throws ParseException {
//		List<Predicate> predicates = new ArrayList<>();
//		if (!AppUtility.isEmpty(userName))
//			predicates.add(builder.like(builder.lower(root.get("name")), "%" + userName.toLowerCase() + "%"));
//
//		return predicates;
//	}

    @Override
    public List<User> findByUsername(String username) {
        return userRepository.findByUsernameForSearch(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserAddress> saveOrUpdateAddress(AddressFilter addressFilter) {
        try {
            if (addressFilter != null) {
                UserAddress userAddress = new UserAddress();
                userAddress.setAddress(addressFilter.getAddress());
                userAddress.setCity(addressFilter.getCity());
                userAddress.setState(addressFilter.getState());
                userAddress.setPincode(addressFilter.getPincode());
                userAddress.setPhone(addressFilter.getPhone());
                userAddress.setAddressType(addressFilter.getAddressType());
                userAddress.setStatus(true);
                User user = this.userRepository.findUserById(addressFilter.getUserId());
//                user.setId(addressFilter.getUserId());
                userAddress.setUser(user);
                if (addressFilter.getId() != null && addressFilter.getId() > 0) {
                    userAddressRepository.findById(addressFilter.getId()).ifPresent(exists -> {
                        userAddress.setId(addressFilter.getId());
                        userAddress.setCreatedBy(exists.getCreatedBy());
                        userAddress.setCreatedDate(exists.getCreatedDate());
                    });
                }
                userAddressRepository.save(userAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            logService.saveErrorLog("Error On Saving User Addres", "UserController", "saveOrUpdateAddress", e.getMessage());
            throw new GenricException("Error On Saving User Addres");

        }
        return userAddressRepository.getAddress(addressFilter.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserAddress> deleteAddress(Long id, Long userId) {
        userAddressRepository.findById(id).ifPresent(idExists -> {
            userAddressRepository.updateAddressStatus(id, userId);
        });
        return userAddressRepository.getAddress(userId);
    }

    @Override
    public List<UserAddress> getAddress(Long userId) {
        return userAddressRepository.getAddress(userId);
    }

    @Override
    public UserAddress findAddressById(Long id) {
        return userAddressRepository.findUserAddressById(id);
    }

    @Override
    public User validatePhoneNumber(String mobileNumber) {
        if (userRepository.existsByWhatsappMobileNumber(mobileNumber)) {
            return userRepository.checkMobileNumber(mobileNumber);
        }
        return null;
    }

    @Override
    public OneTimePassword verifyUserWhatsappNumber(String whatsappMobileNumber, String oneTimePassword) {

        OneTimePassword otp = otpRepository.findByWhatsappMobileNumber(whatsappMobileNumber);
        if (otp != null) {
            if (oneTimePassword.trim().equals(String.valueOf(otp.getOneTimePassword()))) {


            } else {
                throw new GenricException("Wrong OTP Entered");
            }
        } else {
            throw new GenricException("OTP entered is expired. Please generate a new OTP and try again");
        }

        return otp;
    }

    @Override
    public OneTimePassword sendOtp(String whatsappMobileNumber) {
        int randomNumber = 0;
        OneTimePassword oneTimePassword =  new OneTimePassword();
        randomNumber = (int) (Math.random() * 9999);
        if (randomNumber <= 1000) {
            randomNumber = randomNumber + 1000;
        }
        try {
            if (!AppUtility.isEmpty(whatsappMobileNumber)) {
                OneTimePassword otp = otpRepository.findByWhatsappMobileNumber(whatsappMobileNumber);
                if (!AppUtility.isEmpty(otp)){
                    oneTimePassword.setCreatedBy(otp.getCreatedBy());
                    oneTimePassword.setCreatedDate(otp.getCreatedDate());
                    oneTimePassword.setId(otp.getId());
                }
                oneTimePassword.setOneTimePassword(randomNumber);
                oneTimePassword.setWhatsappMobileNumber(whatsappMobileNumber);
                YellowMessengerAPIIntegration.sendOtpOnWhatsapp(whatsappMobileNumber, randomNumber);
                otpRepository.save(oneTimePassword);
            }
        } catch (Exception e) {
            throw new GenricException("Number doesn't exist on whatsapp");
        }
        log.info(String.valueOf(randomNumber));
        return oneTimePassword;
    }

    @Override
    public String saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new GenricException(MsgConstants.ERROR_EMAIL_ALREADY_EXIST);
        }
        Role role = roleRepository.findById(4l).get();
        Set<Role> set = new HashSet<>();
        set.add(role);
        user.setRoles(set);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        this.userRepository.save(user);
       // User u = userRepository.findUserById(user.getId());
        return "Created User Successfully";
    }

    @Override
   public List<User> listRole(String userRole) {
      Role role = roleRepository.findByName(userRole);

        return userRepository.findByRole(userRole);
    }

    @Override
    public void changePassword(PasswordChange passwordChange) {
        User user = userRepository.findByUsername(passwordChange.getUsername());

        if (!Objects.isNull(user)) {
            if(passwordEncoder.matches(passwordChange.getCurrentPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(passwordChange.getNewPassword()));
                userRepository.save(user);
            }else{
                throw new GenricException("Password does not match.");
            }

        }else {
            throw new GenricException("User not found.");
        }

    }


    public void sendMail(User user) {
        smtpMailRest.sendMail(false, "", toEmails, "New User Registered Successfully",
                "Dear Admin,\r\n" +
                        "\r\n" +
                        "New user has registered on your website www.mahavikreta.com \r\n" +
                        "\r\n" +
                        "Full Name : " + user.getFirstName() + " " + user.getLastName() + "\r\n" +
                        "Username : " + user.getUsername() + "\r\n" +
                        "Phone : " + user.getWhatsappMobileNumber() + " \r\n" +
                        "GST : " + user.getGstin() + "\r\n" +
                        "\r\n" +
                        "Thanks and Regards,\r\n" +
                        "Mahavikreta\r\n" +
                        "\r\n",
                false, false, null, null,
                null, new StringBuffer(" "), false);
    }

}
