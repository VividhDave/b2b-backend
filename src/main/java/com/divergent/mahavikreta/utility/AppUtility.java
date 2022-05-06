package com.divergent.mahavikreta.utility;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.divergent.mahavikreta.repository.ProductRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import com.divergent.mahavikreta.constants.AppConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is the utility class for validating different type of objects.
 * 
 * @author Aakash
 *
 */

public class AppUtility {



	private AppUtility() {
	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	/**
	 * To validate email
	 * 
	 * @param text {@link String}
	 * @return {@link Boolean} is invalid or valid
	 */
	public static boolean isEmail(String text) {
		if (isEmpty(text)) {
			return false;
		}
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
		return matcher.find();
	}

	/**
	 * To Validate Collection
	 * 
	 * @param collection {@link Collection}
	 * @return {@link Boolean} is invalid or valid
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * To Validate Map
	 * 
	 * @param map {@link Map}
	 * @return {@link Boolean} is invalid or valid
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * To Validate Object
	 * 
	 * @param object {@link Object}
	 * @return {@link Boolean} is invalid or valid
	 */
	public static boolean isEmpty(Object object) {
		return (object == null);
	}

	/**
	 * To Validate Object array
	 * 
	 * @param array {@link Object} array
	 * @return {@link Boolean} is invalid or valid
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * To Validate String
	 * 
	 * @param string {@link String}
	 * @return {@link Boolean} is invalid or valid
	 */
	public static boolean isEmpty(String string) {
		return (string == null || string.trim().length() == 0);
	}

	/**
	 * To Convert String to jsonNode {@link JsonNode}
	 * 
	 * @param text {@link String}
	 * @return {@link JsonNode}
	 * @throws IOException
	 */
	public static JsonNode converteStringToJson(String text) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readTree(text);
	}

	/**
	 * To convert Object
	 * 
	 * @param params {@link Map}
	 * @param clazz  {@link Class}
	 * @return {@link Object}
	 * @throws JsonProcessingException
	 */
	public static Object getObject(Map<String, Object> params, Class<?> clazz) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode json = objectMapper.convertValue(params, JsonNode.class);
		return objectMapper.treeToValue(json, clazz);
	}

	/**
	 * To Check is Admin or not
	 * 
	 * @return {@link Boolean} is invalid or valid
	 */
	@SuppressWarnings("unchecked")
	public static boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication
				.getDetails();
		Map<String, Object> userMap = (Map<String, Object>) oAuth2AuthenticationDetails.getDecodedDetails();

		List<String> roles = (List<String>) userMap.get("role");
		boolean isAdmin = false;
		for (String role : roles) {
			if (role.equals(AppConstants.ROLE_ADMIN)) {
				isAdmin = true;
			}
		}
		return isAdmin;
	}

	/**
	 * To get Company id
	 * 
	 * @return {@link String}
	 */
	public static String getCompanyId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication
				.getDetails();
		@SuppressWarnings("unchecked")
		Map<String, Object> userMap = (Map<String, Object>) oAuth2AuthenticationDetails.getDecodedDetails();
		return (String) userMap.get("companyId");
	}

	/**
	 * To Compare value
	 * 
	 * @param value1 {@link String}
	 * @param value2 {@link List} {@link String}
	 * @return {@link String}
	 */
	public static String compareValue(String value1, List<String> value2) {
		for (Iterator<String> iterator = value2.iterator(); iterator.hasNext();) {
			String value3 = (String) iterator.next();
			if (value1 == value3 || value1.equals(value3) || value1.toLowerCase().equals(value3.toLowerCase())
					|| value1.replaceAll("\\s+", "").equals(value3.replaceAll("\\s+", ""))) {
				return value3;
			}
		}
		return null;

	}

	/**
	 * To check null string key on JSON
	 * 
	 * @param json {@link JSONObject}
	 * @param key  {@link String}
	 * @return {@link String}
	 */
	public static String checkNullKey(final JSONObject json, final String key) {
		return json.isNull(key) ? null : json.optString(key);
	}

	/**
	 * To check null boolean key on JSON
	 * 
	 * @param json {@link JSONObject}
	 * @param key  {@link String}
	 * @return {@link Boolean} is invalid or valid
	 */
	public static boolean checkBooleanNullKey(final JSONObject json, final String key) {
		return json.isNull(key) ? null : json.getBoolean(key);
	}

	/**
	 * To check null Double key on JSON
	 * 
	 * @param json {@link JSONObject}
	 * @param key  {@link String}
	 * @return {@link Double} value
	 */
	public static Double checkDoubleNullKey(final JSONObject json, final String key) {
		return json.isNull(key) ? null : json.getDouble(key);
	}

	/**
	 * To check null integer key on JSON
	 * 
	 * @param json {@link JSONObject}
	 * @param key  {@link String}
	 * @return {@link Integer}
	 */
	public static int checkIntNullKey(final JSONObject json, final String key) {
		return json.isNull(key) ? null : json.getInt(key);
	}

	/**
	 * To check null float key on JSON
	 * 
	 * @param json {@link JSONObject}
	 * @param key  {@link String}
	 * @return {@link Float}
	 */
	public static float checkFlotNullKey(final JSONObject json, final String key) {
		return json.isNull(key) ? null : json.getFloat(key);
	}

}
