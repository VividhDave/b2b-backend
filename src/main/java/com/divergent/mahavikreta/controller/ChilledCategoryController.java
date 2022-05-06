package com.divergent.mahavikreta.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.ChilledCategory;
import com.divergent.mahavikreta.entity.SubCategory;
import com.divergent.mahavikreta.service.ChilledCategoryService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide ChilledCategory related Rest API
 * 
 * @see ChilledCategoryService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.CHILLED_CATEGORY)
public class ChilledCategoryController {

	@Autowired
	ChilledCategoryService chilledCategoryService;

	/**
	 * This method provides an API for Add ChilledCategory. This method accept Post
	 * Http request with {@link ChilledCategory} and returns ChilledCategory data.
	 * 
	 * @param chilledCategory {@link ChilledCategory} object
	 * @return {@link ChilledCategory}
	 * 
	 * @see ChilledCategoryService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<ChilledCategory> save(@Valid @RequestBody ChilledCategory chilledCategory) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.CHILLED_CATEGORY_CREATED_SUCCESSFULLY,
				chilledCategoryService.save(chilledCategory));
	}

	/**
	 * This method provides an API for Get all ChilledCategory list. This method
	 * accept Get Http request.
	 * 
	 * @return {@link List<ChilledCategory>}
	 * 
	 * @see ChilledCategoryService
	 */
	@GetMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<ChilledCategory>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), chilledCategoryService.getAllRecord());
	}

	/**
	 * This method provides an API for Get ChilledCategory by id. This method accept
	 * Get Http request with id {@link ChilledCategory} and returns ChilledCategory
	 * data.
	 * 
	 * @param id {@link ChilledCategory}
	 * 
	 * @return {@link ChilledCategory}
	 * 
	 * @see ChilledCategoryService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<ChilledCategory> findChilledCategoryById(@RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), chilledCategoryService.findChilledCategoryById(id));
	}

	/**
	 * This method provides an API for Get all ChilledCategory list. This method
	 * accept Post Http request with pageSize, sortOrder. sortValue and
	 * chilledCategoryName and return PageImpl object.
	 * 
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param chilledCategoryName
	 * @param sortOrder
	 * @param sortValue
	 * 
	 * @return {@link PageImpl<ChilledCategory>}
	 * @throws ParseException
	 * 
	 * @see ChilledCategoryService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<ChilledCategory>> getCategoryList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@Valid @RequestParam(required = false, name = "chilledCategoryName") String chilledCategoryName,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), chilledCategoryService.getList(
				ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), chilledCategoryName));
	}

	/**
	 * This method provides an API for Get ChilledCategory by subCategoryId. This
	 * method accept Get Http request with subCategoryId {@link SubCategory} and
	 * returns ChilledCategory data.
	 * 
	 * @param subCategoryId {@link SubCategory}
	 * 
	 * @return {@link ChilledCategory}
	 * 
	 * @see ChilledCategoryService
	 */
	@GetMapping(UriConstants.GET_BY_SUB_CAT_ID)
	public ResponseMessage<List<ChilledCategory>> getByCategoryId(@Valid @RequestParam("subCategoryId") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), chilledCategoryService.getBySubCategoryId(id));
	}

	/**
	 * This method provides an API for save ChilledCategory image. This method
	 * accept Post Http request with file {@link MultipartFile} object, categoryId
	 * {@link ChilledCategory} and return ChilledCategory object.
	 * 
	 * 
	 * @param file       {@link MultipartFile}
	 * @param categoryId {@link ChilledCategory}
	 * 
	 * @return {@link ChilledCategory}
	 * @throws IOException
	 * 
	 * @see ChilledCategoryService
	 */
	@PostMapping(UriConstants.SAVE_IMAGE)
	public ResponseMessage<ChilledCategory> saveImage(@Valid @RequestParam("file") MultipartFile file,
			@Valid @RequestParam("chilledCategoryId") Long chilledCategoryId) throws IOException {
		return new ResponseMessage<>(HttpStatus.OK.value(), "Chilled CategoryId image saved successfully",
				chilledCategoryService.saveImage(file, chilledCategoryId));
	}
}
