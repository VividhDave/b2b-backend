package com.divergent.mahavikreta.utility;

import com.divergent.mahavikreta.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class YellowMessengerAPIIntegration {

    public static String paramBotId;

    public static String headerAuthToken;

    public static String headerCookie;

    public static String languagePolicy;

    public static String languageCode;

    public static String hsmNameSpace;

    public static String ttl;

    @Value("${yellow.messenger.param.bot-id}")
    public void setParamBotId(String paramBotId) {
        YellowMessengerAPIIntegration.paramBotId = paramBotId;
    }

    @Value("${yellow.messenger.http.header.x-auth-token}")
    public void setHeaderAuthToken(String headerAuthToken) {
        YellowMessengerAPIIntegration.headerAuthToken = headerAuthToken;
    }

    @Value("${yellow.messenger.http.header.cookie}")
    public void setHeaderCookie(String headerCookie) {
        YellowMessengerAPIIntegration.headerCookie = headerCookie;
    }

    @Value("${yellow.messenger.language.policy}")
    public void setLanguagePolicy(String languagePolicy) {
        YellowMessengerAPIIntegration.languagePolicy = languagePolicy;
    }

    @Value("${yellow.messenger.language.code}")
    public void setLanguageCode(String languageCode) {
        YellowMessengerAPIIntegration.languageCode = languageCode;
    }

    @Value("${yellow.messenger.hsm.namespace}")
    public void setHsmNameSpace(String hsmNameSpace) {
        YellowMessengerAPIIntegration.hsmNameSpace = hsmNameSpace;
    }

    @Value("${yellow.messenger.ttl}")
    public void setTtl(String ttl) {
        YellowMessengerAPIIntegration.ttl = ttl;
    }

    //send otp on valid whatsapp number
    public static void sendOtpOnWhatsapp(String whatsappMobileNumber, int oneTimePassword) {
        try {
            String curl = AppConstants.YELLOW_MESSENGER_CURL + paramBotId;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(AppConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.set(AppConstants.X_AUTH_TOKEN, headerAuthToken);
            httpHeaders.set(AppConstants.COOKIE, headerCookie);
            JSONObject request = new JSONObject();
            JSONObject body = new JSONObject();
            JSONObject hsm = new JSONObject();
            JSONObject language = new JSONObject();
            language.put(AppConstants.POLICY, languagePolicy);
            language.put(AppConstants.CODE, languageCode);
            Map<String, String> localizable_params = new HashMap<>();
            localizable_params.put(AppConstants.DEFAULT, String.valueOf(oneTimePassword));
            hsm.put(AppConstants.NAMESPACE, hsmNameSpace);
            hsm.put(AppConstants.ELEMENT_NAME, "otp");
            hsm.put(AppConstants.LANGUAGE, language);
            hsm.put(AppConstants.LOCALIZABLE_PARAMS, Collections.singletonList(localizable_params));
            body.put(AppConstants.TO, AppConstants.ISD_CODE + whatsappMobileNumber);
            body.put(AppConstants.TTL, ttl);
            body.put(AppConstants.TYPE, AppConstants.HSM);
            body.put(AppConstants.HSM, hsm);
            request.put(AppConstants.BODY, body);
            HttpEntity<String> req = new HttpEntity<>(request.toString(), httpHeaders);
            String response = restTemplate.postForObject(curl, req, String.class);
            log.info("response from OTP API : " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //send text msg on whatsapp when the invoice is generated
    public static void invoiceGenerationAPI(String whatsappMobileNumber, String name, String invoiceNumber, String date,
                                            String product, String price, String currentBalance) {
        //added type date
        try {
            String curl = AppConstants.YELLOW_MESSENGER_CURL + paramBotId;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(AppConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.set(AppConstants.X_AUTH_TOKEN, headerAuthToken);
            httpHeaders.set(AppConstants.COOKIE, headerCookie);
            JSONObject request = new JSONObject();
            JSONObject body = new JSONObject();
            JSONObject template = new JSONObject();
            JSONObject language = new JSONObject();
            List<JSONObject> parameters = new ArrayList<>();
            JSONObject nameParameter = new JSONObject();
            JSONObject invoiceNumberParameter = new JSONObject();
            JSONObject dateParameter = new JSONObject();
            JSONObject productParameter = new JSONObject();
            JSONObject priceParameter = new JSONObject();
            JSONObject currentBalanceParameter = new JSONObject();
            nameParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            nameParameter.put(AppConstants.TEXT, name);
            invoiceNumberParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            invoiceNumberParameter.put(AppConstants.TEXT, invoiceNumber);

            dateParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            dateParameter.put(AppConstants.TEXT, date);
            productParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            productParameter.put(AppConstants.TEXT, product);
            priceParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            priceParameter.put(AppConstants.TEXT, price);
            currentBalanceParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            currentBalanceParameter.put(AppConstants.TEXT, currentBalance);
            parameters.add(0, nameParameter);
            parameters.add(1, invoiceNumberParameter);

            parameters.add(2, dateParameter);
            parameters.add(3, productParameter);
            parameters.add(4, priceParameter);
            parameters.add(5, currentBalanceParameter);
            JSONObject components = new JSONObject();
            components.put(AppConstants.TYPE, AppConstants.BODY);
            components.put(AppConstants.PARAMETERS, parameters);
            language.put(AppConstants.POLICY, languagePolicy);
            language.put(AppConstants.CODE, languageCode);
            template.put(AppConstants.NAMESPACE, hsmNameSpace);
            template.put(AppConstants.NAME, "invoice_generation_2");
            template.put(AppConstants.LANGUAGE, language);
            template.put(AppConstants.COMPONENTS, Collections.singletonList(components));
            body.put(AppConstants.TO, AppConstants.ISD_CODE + whatsappMobileNumber);
            body.put(AppConstants.TTL, ttl);
            body.put(AppConstants.TYPE, AppConstants.TEMPLATE);
            body.put(AppConstants.TEMPLATE, template);
            request.put(AppConstants.BODY, body);
            request.put(AppConstants.TYPE, AppConstants.MEDIA_NOTIFICATION);
            HttpEntity<String> req = new HttpEntity<>(request.toString(), httpHeaders);
            String response = restTemplate.postForObject(curl, req, String.class);
            log.info("response from invoiceGenerationAPI : " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //send msg on whatsapp when order placed
    public static void orderReceiveAPI(String whatsappMobileNumber, String name, String deliveryDate) {
        try {
            String curl = AppConstants.YELLOW_MESSENGER_CURL + paramBotId;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(AppConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.set(AppConstants.X_AUTH_TOKEN, headerAuthToken);
            httpHeaders.set(AppConstants.COOKIE, headerCookie);
            JSONObject request = new JSONObject();
            JSONObject body = new JSONObject();
            JSONObject template = new JSONObject();
            JSONObject language = new JSONObject();
            List<JSONObject> parameters = new ArrayList<>();
            JSONObject nameParameter = new JSONObject();
            JSONObject deliveryDateParameter = new JSONObject();
            nameParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            nameParameter.put(AppConstants.TEXT, name);
            deliveryDateParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            deliveryDateParameter.put(AppConstants.TEXT, deliveryDate);
            parameters.add(0, nameParameter);
            parameters.add(1, deliveryDateParameter);
            JSONObject components = new JSONObject();
            components.put(AppConstants.TYPE, AppConstants.BODY);
            components.put(AppConstants.PARAMETERS, parameters);
            language.put(AppConstants.POLICY, languagePolicy);
            language.put(AppConstants.CODE, languageCode);
            template.put(AppConstants.NAMESPACE, hsmNameSpace);
            template.put(AppConstants.NAME, "order_receive_2");
            template.put(AppConstants.LANGUAGE, language);
            template.put(AppConstants.COMPONENTS, Collections.singletonList(components));
            body.put(AppConstants.TO, AppConstants.ISD_CODE + whatsappMobileNumber);
            body.put(AppConstants.TTL, ttl);
            body.put(AppConstants.TYPE, AppConstants.TEMPLATE);
            body.put(AppConstants.TEMPLATE, template);
            request.put(AppConstants.BODY, body);
            request.put(AppConstants.TYPE, AppConstants.MEDIA_NOTIFICATION);
            HttpEntity<String> req = new HttpEntity<>(request.toString(), httpHeaders);
            String response = restTemplate.postForObject(curl, req, String.class);
            log.info("response from orderReceiveAPI : " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //send msg when payment success
    public static void paymentConfirmationAPI(String whatsappMobileNumber, String name, String creditedAmount,
                                              String referenceNumber, String receiptDate, String currentBalance) {
        try {
            String curl = AppConstants.YELLOW_MESSENGER_CURL + paramBotId;
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(AppConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.set(AppConstants.X_AUTH_TOKEN, headerAuthToken);
            httpHeaders.set(AppConstants.COOKIE, headerCookie);
            JSONObject request = new JSONObject();
            JSONObject body = new JSONObject();
            JSONObject template = new JSONObject();
            JSONObject language = new JSONObject();
            List<JSONObject> parameters = new ArrayList<>();
            JSONObject nameParameter = new JSONObject();
            JSONObject creditedAmountParameter = new JSONObject();
            JSONObject referenceNumberParameter = new JSONObject();
            JSONObject receiptDateParameter = new JSONObject();
            JSONObject currentBalanceParameter = new JSONObject();
            nameParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            nameParameter.put(AppConstants.TEXT, name);
            creditedAmountParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            creditedAmountParameter.put(AppConstants.TEXT, creditedAmount);
            referenceNumberParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            referenceNumberParameter.put(AppConstants.TEXT, referenceNumber);
            receiptDateParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            receiptDateParameter.put(AppConstants.TEXT, receiptDate);
            currentBalanceParameter.put(AppConstants.TYPE, AppConstants.TEXT);
            currentBalanceParameter.put(AppConstants.TEXT, currentBalance);
            parameters.add(0, nameParameter);
            parameters.add(1, creditedAmountParameter);
            parameters.add(2, referenceNumberParameter);
            parameters.add(3, receiptDateParameter);
            parameters.add(4, currentBalanceParameter);
            JSONObject components = new JSONObject();
            components.put(AppConstants.TYPE, AppConstants.BODY);
            components.put(AppConstants.PARAMETERS, parameters);
            language.put(AppConstants.POLICY, languagePolicy);
            language.put(AppConstants.CODE, languageCode);
            template.put(AppConstants.NAMESPACE, hsmNameSpace);
            template.put(AppConstants.NAME, "payment_confirmation_2");
            template.put(AppConstants.LANGUAGE, language);
            template.put(AppConstants.COMPONENTS, Collections.singletonList(components));
            body.put(AppConstants.TO, AppConstants.ISD_CODE + whatsappMobileNumber);
            body.put(AppConstants.TTL, ttl);
            body.put(AppConstants.TYPE, AppConstants.TEMPLATE);
            body.put(AppConstants.TEMPLATE, template);
            request.put(AppConstants.BODY, body);
            request.put(AppConstants.TYPE, AppConstants.MEDIA_NOTIFICATION);
            HttpEntity<String> req = new HttpEntity<>(request.toString(), httpHeaders);
            String response = restTemplate.postForObject(curl, req, String.class);
            log.info("response from paymentConfirmationAPI : " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}