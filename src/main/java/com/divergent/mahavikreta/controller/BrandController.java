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
import com.divergent.mahavikreta.entity.Brand;
import com.divergent.mahavikreta.service.BrandService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Brand related Rest API
 * 
 * @see BrandService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.BRAND)
public class BrandController {

	@Autowired
	BrandService brandService;

	/**
	 * This method provides an API for Add Brand. This method accept Post Http
	 * request with {@link Brand} and returns Brand data.
	 * 
	 * @param brand {@link Brand}
	 * @return {@link Brand}
	 * 
	 * @see BrandService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<Brand> save(@Valid @RequestBody Brand brand) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.BRAND_CREATED_SUCCESSFULLY,
				brandService.save(brand));
	}

	/**
	 * This method provides an API for Get all brand list. This method accept Get
	 * Http request.
	 * 
	 * 
	 * @return {@link List<Brand>}
	 * 
	 * @see BrandService
	 */
	@GetMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<Brand>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), brandService.getAllRecord());
	}

	/**
	 * This method provides an API for Get Brand by id. This method accept Get Http
	 * request with id {@link Brand} and returns Brand data.
	 * 
	 * @param id {@link Brand}
	 * 
	 * @return {@link Brand}
	 * 
	 * @see BrandService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<Brand> findBrandById(@RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), brandService.findBrandById(id));
	}
	
	/**
	 * This method provides an API for Get all brand list. This method accept Post Http request
	 * with pageSize, sortOrder. sortValue and brandName and return PageImpl object.
	 * 
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param brandName
	 * @param sortOrder
	 * @param sortValue 
	 * 
	 * @return {@link PageImpl<Brand>}
	 * @throws ParseException
	 * 
	 * @see BrandService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<Brand>> getCategoryList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@Valid @RequestParam(required = false, name = "brandName") String brandName,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), brandService
				.getList(ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), brandName));
	}

	/**
	 * This method provides an API for save brand image. This method accept Post Http request
	 * with file {@link MultipartFile} object, brandId {@link Brand} and return Brand object.
	 * 
	 * 
	 * @param file {@link MultipartFile}
	 * @param brandId {@link Brand}
	 * 
	 * @return {@link Brand}
	 * @throws IOException
	 * 
	 * @see BrandService
	 */
	@PostMapping(UriConstants.SAVE_IMAGE)
	public ResponseMessage<Brand> saveImage(@Valid @RequestParam("file") MultipartFile file,
			@Valid @RequestParam("brandId") Long brandId) throws IOException {
		return new ResponseMessage<>(HttpStatus.OK.value(), "Brand image saved successfully",
				brandService.saveImage(file, brandId));
	}
}
