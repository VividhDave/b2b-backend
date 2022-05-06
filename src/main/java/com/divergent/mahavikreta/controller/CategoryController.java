package com.divergent.mahavikreta.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Category;
import com.divergent.mahavikreta.service.CategoryService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Category related Rest API
 * 
 * @see CategoryService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.CATEGORY)
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	/**
	 * This method provides an API for Add Category. This method accept Post Http
	 * request with {@link Category} and returns Category data.
	 * 
	 * @param category {@link Category} object
	 * @return {@link Category}
	 * 
	 * @see CategoryService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<Category> save(@Valid @RequestBody Category category) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.CATEGORY_CREATED_SUCCESSFULLY,
				categoryService.save(category));
	}

	/**
	 * This method provides an API for Get all Category list. This method accept Get
	 * Http request.
	 * 
	 * @return {@link List<Category>}
	 * 
	 * @see CategoryService
	 */
	@GetMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<Category>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), categoryService.getAllRecord());
	}

	/**
	 * This method provides an API for Get Category by id. This method accept Get
	 * Http request with id {@link Category} and returns Category data.
	 * 
	 * @param id {@link Category}
	 * 
	 * @return {@link Category}
	 * 
	 * @see CategoryService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<Category> findCategoryById(@RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), categoryService.findCategoryById(id));
	}

	/**
	 * This method provides an API for Get all Category list. This method accept
	 * Post Http request with pageSize, sortOrder. sortValue and categoryName and
	 * return PageImpl object.
	 * 
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param categoryName
	 * @param sortOrder
	 * @param sortValue
	 * 
	 * @return {@link PageImpl<Category>}
	 * @throws ParseException
	 * 
	 * @see CategoryService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<Category>> getCategoryList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@Valid @RequestParam(required = false, name = "categoryName") String categoryName,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), categoryService
				.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), categoryName));
	}

	/**
	 * This method provides an API for save Category image. This method accept Post
	 * Http request with file {@link MultipartFile} object, categoryId
	 * {@link Category} and return Category object.
	 * 
	 * 
	 * @param file       {@link MultipartFile}
	 * @param categoryId {@link Category}
	 * 
	 * @return {@link Category}
	 * @throws IOException
	 * 
	 * @see CategoryService
	 */
	@PostMapping(UriConstants.SAVE_IMAGE)
	public ResponseMessage<Category> saveImage(@Valid @RequestParam("file") MultipartFile file,
			@Valid @RequestParam("categoryId") Long categoryId) throws IOException {
		return new ResponseMessage<>(HttpStatus.OK.value(), "Category image saved successfully",
				categoryService.saveImage(file, categoryId));
	}
	@DeleteMapping(UriConstants.DELETE)
	public ResponseMessage<List> deleteCategory(@Valid @RequestParam(name = "id")long id){
		this.categoryService.deleteCategoryById(id);
		return new ResponseMessage<>(HttpStatus.OK.value(),MsgConstants.CATEGORY_DELETED);
	}
}
