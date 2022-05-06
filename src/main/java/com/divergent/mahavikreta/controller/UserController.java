package com.divergent.mahavikreta.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

// import com.divergent.mahavikreta.entity.CustomerUserPrivilege;
import com.divergent.mahavikreta.entity.OneTimePassword;
import com.divergent.mahavikreta.entity.Role;
import com.divergent.mahavikreta.entity.UserAddress;
import com.divergent.mahavikreta.entity.filter.AddressFilter;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.payload.PasswordChange;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.service.UserService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;
import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;

/**
 * This class provide User related API
 *
 * @author Aakash
 * @see UserService
 */
@RestController
@RequestMapping(UriConstants.USER)
public class UserController {

    @Autowired
    UserService userService;

    /**
     * This method provides an API for save User. This method accept Post Http
     * request with request user {@link User} and return User.
     *
     * @param user {@link User}
     * @return {@link User}
     * @see UserService
     */
    @PostMapping(UriConstants.SAVE)
    public ResponseMessage<User> save(@Valid @RequestBody User user) throws RazorpayException {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.USER_CREATED_SUCCESSFULLY,
                userService.save(user));
    }

    /**
     * This method provides an API for update User by id. This method accept Put
     * Http request with request user {@link User},id and return User.
     *
     * @param user {@link User}
     * @param id   {@link Long}
     * @return {@link User}
     * @see UserService
     */
    @PutMapping(UriConstants.UPDATE)
    public ResponseMessage<User> update(@Valid @RequestBody User user, @Valid @RequestParam("id") Long id) {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.USER_UPDATE_SUCCESSFULLY,
                userService.update(id, user));
    }

    /**
     * This method provides an API for find User by id. This method accept Get Http
     * request with request id and return User.
     *
     * @param id {@link Long}
     * @return {@link User}
     * @see UserService
     */
    @GetMapping(UriConstants.FIND_BY_ID)
    public ResponseMessage<User> findById(@Valid @RequestParam("id") Long id) {
        return new ResponseMessage<>(HttpStatus.OK.value(), userService.findById(id));
    }

    /**
     * This method provides an API for all User list. This method accept Post Http
     * request with request pageIndex, pageSize, sortOrder, sortValue and return
     * PageImpe object.
     *
     * @param pageIndex {@link Integer}
     * @param pageSize  {@link Integer}
     * @param sortOrder {@link String}
     * @param sortValue {@link String}
     * @return {@link PageImpl}
     * @throws ParseException
     * @see UserService
     */
    @PostMapping(UriConstants.GET_ALL)
    public ResponseMessage<PageImpl<User>> getUserList(@RequestParam("pageIndex") int pageIndex,
                                                       @RequestParam("pageSize") int pageSize,
                                                       @RequestParam(required = false, name = "sortOrder") String sortOrder,
                                                       @RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
        return new ResponseMessage<>(HttpStatus.OK.value(),
                userService.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue)));
    }

    /**
     * This method provides an API for find User by name. This method accept Get
     * Http request with request name and return User list.
     *
     * @param username {@link String}
     * @return {@link List}
     * @see UserService
     */
    @PostMapping(UriConstants.FIND_BY_USERNAME)
    public ResponseMessage<List<User>> findByUsername(@Valid @RequestParam("username") String username) {
        return new ResponseMessage<>(HttpStatus.OK.value(), userService.findByUsername(username));
    }

    /**
     * This method provides an API for save User address. This method accept Post
     * Http request with request addressFilter {@link AddressFilter} and return list
     * of UserAddress.
     *
     * @param addressFilter {@link AddressFilter}
     * @return {@link List}
     * @see UserService
     */
    @PostMapping(UriConstants.SAVE_ADDRESS)
    public ResponseMessage<List<UserAddress>> saveOrUpdateAddress(@Valid @RequestBody AddressFilter addressFilter) {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.SAVE_ADDRESS,
                userService.saveOrUpdateAddress(addressFilter));
    }

    /**
     * This method provides an API for soft delete User address. This method accept
     * Delete Http request with request id {@link UserAddress}, userId {@link User}
     * and return list of UserAddress.
     *
     * @param id     {@link Long}
     * @param userId {@link Long}
     * @return {@link List<UserAddress>}
     * @see UserService
     */
    @DeleteMapping(UriConstants.DELETE_ADDRESS)
    public ResponseMessage<List<UserAddress>> deleteAddress(@Valid @RequestParam(name = "id") Long id,
                                                            @Valid @RequestParam(name = "userId") Long userId) {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.CARD_PRODUCT_DELETE,
                userService.deleteAddress(id, userId));
    }

    /**
     * This method provides an API for get User address list by user id. This method
     * accept Get Http request with request userId {@link User} and return list of
     * UserAddress.
     *
     * @param userId {@link Long}
     * @return {@link List}
     * @see UserService
     */
    @GetMapping(UriConstants.GET_ADDRESS)
    public ResponseMessage<List<UserAddress>> getAddress(@Valid @RequestParam("userId") Long userId) {
        return new ResponseMessage<>(HttpStatus.OK.value(), userService.getAddress(userId));
    }

    /**
     * This method provides an API for find User address by id. This method accept
     * Get Http request with request id and return UserAddress object.
     *
     * @param id {@link Long}
     * @return {@link UserAddress}
     * @see UserService
     */
    @GetMapping(UriConstants.FIND_ADDRESS_BY_ID)
    public ResponseMessage<UserAddress> findAddressById(@Valid @RequestParam("id") Long id) {
        return new ResponseMessage<>(HttpStatus.OK.value(), userService.findAddressById(id));
    }

    /**
     * This method provides an API for validate User phone number. This method accept
     * Get Http request with request mobile number and return UserAddress object.
     *
     * @param mobileNumber {@link String}
     * @return {@link User}
     * @see UserService
     */
    @GetMapping(UriConstants.VALIDATE_PHONE_NUMBER)
    public ResponseMessage<User> validatePhoneNumber(@Valid @RequestParam("mobileNumber") String mobileNumber) {
        User user = userService.validatePhoneNumber(mobileNumber);
        return new ResponseMessage<>(HttpStatus.OK.value(), userService.validatePhoneNumber(mobileNumber));
    }

    /**
     * This method provides an API for Verify User Whatsapp number and allow user to login. This method accept
     * Post Http request with user id and OTP and return User.
     *
     * @param oneTimePassword {@link String}
     * @return {@link User}
     * @see UserService
     */
    @PostMapping(UriConstants.OTP_VERIFY)
    public ResponseMessage<OneTimePassword> verifyUserWhatsappNumber(@Valid @RequestParam("whatsappMobileNumber") String whatsappMobileNumber,
                                                                     @Valid @RequestParam("oneTimePassword") String oneTimePassword) {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.USER_VERIFY,
                userService.verifyUserWhatsappNumber(whatsappMobileNumber, oneTimePassword));
    }

    /**
     * This method provides an API for resend otp for Verify User Whatsapp number and allow user to login.
     * This method accept Post Http request with user id and whatsapp number and return new otp.
     *
     * @param whatsappMobileNumber {@link String}
     * @return {@link Integer}
     * @see UserService
     */
    @PostMapping(UriConstants.SEND_OTP)
    public ResponseMessage<OneTimePassword> sendOtp(@Valid @RequestParam("whatsappMobileNumber") String whatsappMobileNumber) {
        return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.OTP_SEND,
                userService.sendOtp(whatsappMobileNumber));
    }

    @PutMapping(UriConstants.WEB_SAVE)
    public ResponseMessage<User> saveUser(@Valid @RequestBody User user){
        return new ResponseMessage<User>(HttpStatus.OK.value(),userService.saveUser(user));
    }

    @PostMapping(UriConstants.USER_LIST)
    public ResponseMessage<List<User>>listByRole(@RequestParam("userRole") String userRole){
        return new ResponseMessage<>(HttpStatus.OK.value(), userService.listRole(userRole));
    }

    @PostMapping(UriConstants.CHANGE_PASSWORD)
    public ResponseMessage<String> changePassword(@RequestBody PasswordChange passwordChange) {
        if (passwordChange.getNewPassword().length()==0) {
            throw new GenricException("new password is null");
        }
        userService.changePassword(passwordChange);
        return new ResponseMessage<>(HttpStatus.OK.value(), "Password Changed successfully");
    }

}
