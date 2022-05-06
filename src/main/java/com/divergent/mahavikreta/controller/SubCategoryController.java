package com.divergent.mahavikreta.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Category;
import com.divergent.mahavikreta.entity.SubCategory;
import com.divergent.mahavikreta.service.SubCategoryService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide SubCategory related Rest API
 * 
 * @see SubCategoryService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.SUBCATEGORY)
public class SubCategoryController {

	@Autowired
	SubCategoryService subCategoryService;

	/**
	 * This method provides an API for Add SubCategory. This method accept Post Http
	 * request with {@link SubCategory} and returns SubCategory data.
	 * 
	 * @param subCategory {@link SubCategory} object
	 * @return {@link SubCategory}
	 * 
	 * @see SubCategoryService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<SubCategory> save(@Valid @RequestBody SubCategory subCategory) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.CATEGORY_CREATED_SUCCESSFULLY,
				subCategoryService.save(subCategory));
	}

	/**
	 * This method provides an API for Get all SubCategory list. This method accept
	 * Get Http request.
	 * 
	 * @return {@link List<SubCategory>}
	 * 
	 * @see SubCategoryService
	 */
	@GetMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<SubCategory>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), subCategoryService.getAllRecord());
	}

	/**
	 * This method provides an API for Get SubCategory by id. This method accept Get
	 * Http request with id {@link SubCategory} and returns SubCategory data.
	 * 
	 * @param id {@link SubCategory}
	 * 
	 * @return {@link SubCategory}
	 * 
	 * @see SubCategoryService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<SubCategory> findSubCategoryById(@RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), subCategoryService.findSubCategoryById(id));
	}

	/**
	 * This method provides an API for Get all SubCategory list. This method accept
	 * Post Http request with pageSize, sortOrder. sortValue and chilledCategoryName
	 * and return PageImpl object.
	 * 
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param subCategoryName
	 * @param sortOrder
	 * @param sortValue
	 * 
	 * @return {@link PageImpl<SubCategory>}
	 * @throws ParseException
	 * 
	 * @see SubCategoryService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<SubCategory>> getCategoryList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@Valid @RequestParam(required = false, name = "subCategoryName") String subCategoryName,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), subCategoryService
				.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), subCategoryName));
	}

	/**
	 * This method provides an API for Get SubCategory by categoryId. This method
	 * accept Get Http request with categoryId {@link Category} and returns
	 * SubCategory data.
	 * 
	 * @param categoryId {@link Category}
	 * 
	 * @return {@link SubCategory}
	 * 
	 * @see SubCategoryService
	 */
	@GetMapping(UriConstants.GET_BY_CAT_ID)
	public ResponseMessage<List<SubCategory>> getByCategoryId(@Valid @RequestParam("categoryId") Long categoryId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), subCategoryService.getByCategoryId(categoryId));
	}

	/**
	 * This method provides an API for save SubCategory image. This method accept
	 * Post Http request with file {@link MultipartFile} object, categoryId
	 * {@link SubCategory} and return SubCategory object.
	 * 
	 * 
	 * @param file       {@link MultipartFile}
	 * @param categoryId {@link SubCategory}
	 * 
	 * @return {@link SubCategory}
	 * @throws IOException
	 * 
	 * @see SubCategoryService
	 */
	@PostMapping(UriConstants.SAVE_IMAGE)
	public ResponseMessage<SubCategory> save(@Valid @RequestParam("file") MultipartFile file,
			@Valid @RequestParam("subCategoryId") Long subCategoryId) throws IOException {
		return new ResponseMessage<>(HttpStatus.OK.value(), "SubCategory image saved successfully",
				subCategoryService.saveImage(file, subCategoryId));
	}
	@DeleteMapping(UriConstants.DELETE)
	public ResponseMessage<List> deleteSubCategory(@Valid @RequestParam(name = "id")long id){
		this.subCategoryService.deleteSubCategoryById(id);
		return new ResponseMessage<>(HttpStatus.OK.value(),MsgConstants.SUB_CATEGORY_DELETED);
	}
}
