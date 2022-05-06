package com.divergent.mahavikreta.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.sun.javafx.scene.traversal.Direction;

/**
 * This is the utility class for converting different type of objects.
 * 
 * @author Aakash
 *
 */
public class ConversionUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConversionUtils.class);

	private ConversionUtils() {

	}

	/**
	 * To Convert {@link Object} to {@link Map}
	 * 
	 * @param object {@link Object}
	 * @param level {@link Integer}
	 * @return {@link Map} 
	 */
	public static Map<String, Object> convertEntityToMap(Object object, int level) {
		Map<String, Object> result = new HashMap<>();
		switch (level) {
		case 0:
		case 1:
			convertEntityToMapR2(object, result);
			break;
		case 2:
			convertEntityToMapR1(object, result);
			break;
		default:
			convertEntityToMapR(object, result);
		}

		return result;
	}

	/**
	 * To Convert {@link List} of {@link Object} into {@link List} {@link Map}
	 * 
	 * @param object {@link List}
	 * @param level {@link Integer}
	 * @return {@link List} {@link Map}
	 */
	public static List<Map<String, Object>> convertEntityToMapList(List<?> object, int level) {
		List<Map<String, Object>> result = new ArrayList<>();
		for (Object obj : object) {
			result.add(convertEntityToMap(obj, level));
		}
		return result;
	}

	/**
	 * To Convert {@link Map} {@link Class} into {@link Object}
	 * 
	 * @param object {@link Map}
	 * @param objClass {@link Class}
	 * @return {@link Object}
	 */
	public static Object convertMapToEntity(Map<String, Object> object, Class<?> objClass) {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return objectMapper.convertValue(object, objClass);

	}

	/**
	 * To Convert {@link Object} and {@link Map} into {@link Map}
	 * 
	 * @param object {@link Object}
	 * @param jgen {@link Map}
	 * @return {@link Map}
	 */
	public static Map<String, Object> convertEntityToMapR(Object object, Map<String, Object> jgen) {
		for (Method m : object.getClass().getMethods()) {
			if (checkMethodeType(m)) {
				try {
					putValuesToMapR(m.invoke(object), jgen, getFieldName(m.getName()));
				} catch (Exception e) {
					LOGGER.error("Error in ConversionUtils.convertEntityToMapR ::", e);
				}
			}
		}
		return jgen;
	}

	/**
	 * To Convert entity to {@link Map}
	 * 
	 * @param object {@link Object}
	 * @param jgen {@link Map}
	 * @return {@link Map}
	 */
	public static Map<String, Object> convertEntityToMapR1(Object object, Map<String, Object> jgen) {
		for (Method m : object.getClass().getMethods()) {
			if (checkMethodeType(m)) {
				try {
					putValuesToMapR1(m.invoke(object), jgen, getFieldName(m.getName()));
				} catch (Exception e) {
					LOGGER.error("Error in ConversionUtils.convertEntityToMapR1 ::", e);
				}
			}
		}
		return jgen;
	}

	/**
	 * To convert Entity to {@link Map}
	 * 
	 * @param object {@link Object}
	 * @param jgen {@link Map}
	 * @return {@link Map}
	 */
	public static Map<String, Object> convertEntityToMapR2(Object object, Map<String, Object> jgen) {
		for (Method m : object.getClass().getMethods()) {
			if (checkMethodeType(m)) {
				try {
					putValuesToMapR2(m.invoke(object), jgen, getFieldName(m.getName()));
				} catch (Exception e) {
					LOGGER.error("Error in ConversionUtils.convertEntityToMapR2 ::", e);
				}
			}
		}
		return jgen;
	}

	/**
	 * To put value to {@link Map}
	 * 
	 * @param value {@link Object}
	 * @param jgen {@link Map}
	 * @param fieldName {@link String}
	 */
	private static void putValuesToMapR(Object value, Map<String, Object> jgen, String fieldName) {
		if (value != null) {
			if (!(value instanceof JavassistLazyInitializer || value instanceof PersistentBag)) {
				if (value.getClass().getPackage().getName().contains("erp")) {
					if (!value.getClass().isEnum()) {
						jgen.put(fieldName, convertEntityToMapR1(value, new HashMap<String, Object>()));
					} else {
						jgen.put(fieldName, value);
					}
				} else if (value instanceof Set) {
					jgen.put(fieldName, getListFromObject(value));
				} else if (value.getClass().isPrimitive()) {
					jgen.put(fieldName, value);
				} else {
					jgen.put(fieldName, (Object) value);
				}
			}
		} else {
			jgen.put(fieldName, null);
		}
	}

	/**
	 * To put value to {@link Map}
	 * 
	 * @param value {@link Object}
	 * @param jgen {@link Map}
	 * @param fieldName {@link String}
	 */
	private static void putValuesToMapR1(Object value, Map<String, Object> jgen, String fieldName) {
		if (value != null) {
			if (!(value instanceof JavassistLazyInitializer || value instanceof PersistentBag)) {
				if (value.getClass().getPackage().getName().contains("erp")) {
					if (!value.getClass().isEnum()) {
						jgen.put(fieldName, convertEntityToMapR2(value, new HashMap<String, Object>()));
					} else {
						jgen.put(fieldName, value);
					}
				} else if (value instanceof Set) {
					jgen.put(fieldName, null);
				} else if (value.getClass().isPrimitive()) {
					jgen.put(fieldName, value);
				} else {
					jgen.put(fieldName, (Object) value);
				}
			}
		} else {
			jgen.put(fieldName, null);
		}
	}

	/**
	 * To put value to {@link Map}
	 * 
	 * @param value {@link Object}
	 * @param jgen {@link Map}
	 * @param fieldName {@link String}
	 */
	private static void putValuesToMapR2(Object value, Map<String, Object> jgen, String fieldName) {
		if (value != null) {
			if (!(value instanceof JavassistLazyInitializer || value instanceof PersistentBag)) {
				if (value.getClass().getPackage().getName().contains("erp")) {
					if (!value.getClass().isEnum()) {
						jgen.put(fieldName, null);
					} else {
						jgen.put(fieldName, value);
					}
				} else if (value instanceof Set) {
					jgen.put(fieldName, null);
				} else if (value.getClass().isPrimitive()) {
					jgen.put(fieldName, value);
				} else {
					jgen.put(fieldName, (Object) value);
				}
			}
		} else {
			jgen.put(fieldName, null);
		}
	}
	

	/**
	 * To Get list form object
	 * 
	 * @param value {@link Object}
	 * @return {@link ArrayList} {@link Map}
	 */
	private static ArrayList<Map<String, Object>> getListFromObject(Object value) {
		ArrayList<Map<String, Object>> list = new ArrayList<>();
		for (Object val : (Set<?>) value) {
			list.add(convertEntityToMapR1(val, new HashMap<String, Object>()));
		}
		return list;
	}

	/**
	 * To field name
	 * 
	 * @param methodName {@link String}
	 * @return {@link String}
	 */
	private static String getFieldName(String methodName) {
		return methodName.startsWith("is") ? methodName.substring(2, 3).toLowerCase() + methodName.substring(3)
				: methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
	}

	/**
	 * To check method type
	 * 
	 * @param m {@link Method}
	 * @return
	 */
	private static boolean checkMethodeType(Method m) {
		return (m.getName().startsWith("get") || m.getName().startsWith("is")) && !m.getName().startsWith("getClass");
	}

	/**
	 * To convert {@link Page} to {@link Map}
	 * 
	 * @param page {@link Page}
	 * @param level {@link Integer}
	 * @return {@link Map}
	 */
	public static Map<String, Object> convertPageToMap(Page<?> page, int level) {
		Map<String, Object> result = new HashMap<>();
		result.put("content", ConversionUtils.convertEntityToMapList(new ArrayList<Object>(page.getContent()), level));
		result.put("totalElements", page.getTotalElements());
		result.put("number", page.getNumber());
		result.put("size", page.getSize());
		result.put("last", page.isLast());
		result.put("totalPages", page.getTotalPages());
		result.put("sort", page.getSort());
		result.put("first", page.isFirst());
		result.put("numberOfElements", page.getNumberOfElements());
		return result;
	}
	
	/**
	 * To create Page Object
	 * 
	 * @param pageSize {@link Integer}
	 * @param pageIndex {@link Integer}
	 * @param sortOrder {@link String}
	 * @param sortValue {@link String}
	 * @return {@link Pageable}
	 */
	public static Pageable createPageObject(int pageSize, int pageIndex, String sortOrder, String sortValue) {
		Pageable pageable = null;
		if (!(AppUtility.isEmpty(sortOrder) || AppUtility.isEmpty(sortValue))) {
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(getSortDirection(sortOrder), sortValue));
			pageable = PageRequest.of(pageIndex, pageSize, Sort.by(orders));
		} else {
			pageable = PageRequest.of(pageIndex, pageSize);
		}
		return pageable;
	}

	/**
	 * To get sort direction
	 * 
	 * @param direction {@link String}
	 * @return {@link Sort} {@link Direction}
	 */
	public static Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")||direction.equals("ASC")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")||direction.equals("DESC")) {
			return Sort.Direction.DESC;
		}

		return Sort.Direction.ASC;
	}
	
	/**
	 * To Convert multipart to file
	 * 
	 * @param file {@link MultipartFile}
	 * @return {@link File}
	 * @throws IOException
	 */
	public static File convertMultiPartToFile(MultipartFile file ) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }

}