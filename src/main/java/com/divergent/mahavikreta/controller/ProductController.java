package com.divergent.mahavikreta.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.filter.ProductFilter;
import com.divergent.mahavikreta.service.ProductService;
import com.divergent.mahavikreta.utility.ConversionUtils;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Product related API
 * 
 * @see ProductService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.PRODUCT)
public class ProductController {

	@Autowired
	ProductService productService;

	/**
	 * This method provides an API for save product. This method accept Post Http
	 * request with request product {@link Map<String, Object>} return Product.
	 * 
	 * @param product {@link Map}
	 * @return {@link Product}
	 * 
	 * @see ProductService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<Product> save(@Valid @RequestBody Map<String, Object> product) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.PRODUCT_CREATED_SUCCESSFULLY,
				productService.save(product));
	}

	/**
	 * This method provides an API for save product. This method accept Post Http
	 * request with request product {@link Product} return Product.
	 * 
	 * 
	 * @param product {@link Product}
	 * @return {@link Product}
	 * 
	 * @see ProductService
	 */
	@PostMapping("/save_product")
	public ResponseMessage<Product> saveProduct(@Valid @RequestBody Product product) {
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.PRODUCT_CREATED_SUCCESSFULLY,
				productService.saveProduct(product));
	}

	/**
	 * This method provides an API for find product by product id. This method
	 * accept Get Http request with request id {@link Product} return
	 * {@link Product}.
	 * 
	 * @param id {@link Long}
	 * @return {@link Product}
	 * 
	 * @see ProductService
	 */
	@GetMapping(UriConstants.FIND_BY_ID)
	public ResponseMessage<Product> findProductById(@Valid @RequestParam("id") Long id) {
		return new ResponseMessage<>(HttpStatus.OK.value(), productService.findById(id));
	}

	/**
	 * This method provides an API for get all product.
	 * 
	 * @return {@link List<Product>}
	 * 
	 * @see ProductService
	 */
	@GetMapping(UriConstants.GET_ALL_RECORD)
	public ResponseMessage<List<Product>> getAll() {
		return new ResponseMessage<>(HttpStatus.OK.value(), productService.getAllRecord());
	}

	/**
	 * This method provides an API for Get all Product list. This method accept Post
	 * Http request with pageIndex, pageSize, sortOrder, sortValue and productFilter
	 * {@link ProductFilter} and return PageImpl object.
	 * 
	 * @param pageIndex     {@link Integer}
	 * @param pageSize      {@link Integer}
	 * @param sortOrder     {@link String}
	 * @param sortValue     {@link String}
	 * @param isImage       {@link Boolean}
	 * @param productFilter {@link ProductFilter}
	 * 
	 * @return {@link PageImpl<Product>}
	 * 
	 * @throws ParseException
	 * 
	 * @see ProductService
	 */
	@PostMapping(UriConstants.GET_ALL)
	public ResponseMessage<PageImpl<Product>> getProductList(@RequestParam("pageIndex") int pageIndex,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(required = false, name = "sortOrder") String sortOrder,
			@RequestParam(required = false, name = "sortValue") String sortValue,
			@RequestParam(required = false, name = "isImage") boolean isImage,
			@RequestBody(required = false) ProductFilter productFilter) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), productService.getList(
				ConversionUtils.createPageObject(pageSize, pageIndex, sortOrder, sortValue), isImage, productFilter));
	}

	/**
	 * This method provides an API for Save all Product using file. This method
	 * accept Post Http request with files {@link MultipartFile}
	 * 
	 * @param file {@link MultipartFile}
	 * @return {@link List<Product>}
	 * 
	 * @see ProductService
	 */
		@PostMapping(UriConstants.SAVE_PRODUCT_USING_EXCEL)
	public ResponseMessage<List<Product>> saveAll(@Valid @RequestParam("files") MultipartFile file) {

		return new ResponseMessage<>(HttpStatus.OK.value(), productService.saveProductUsingExcelFile(file));

	}

	/**
	 * This method provides an API for search Product by product name. This method
	 * accept Get Http request with productName {@link String} and return product
	 * list
	 * 
	 * @param productName {@link String}
	 * @return {@link List<Product>}
	 * 
	 * @see ProductService
	 */
	@GetMapping(UriConstants.SEARCH_BY_PRODUCT_NAME)
	public ResponseMessage<List<Product>> getProductListByProductName(@Valid @RequestParam("productName") String productName) {
		return new ResponseMessage<>(HttpStatus.OK.value(), productService.getProductListByProductName(productName));
	}

	/**
	 * This method provides an API for search Product by product name. This method
	 * accept Get Http request with productName {@link String} and return product
	 * list
	 * 
	 * @param categoryId 
	 * @param subCategoryId {@link String}
	 * @return {@link List<Product>}
	 * 
	 * @see ProductService
	 */
	@GetMapping(UriConstants.SEARCH_BY_CATEGORY_OR_SUBCATEGORY)
	public ResponseMessage<List<Product>> getProductListByCatOrSubCat(Pageable pageable,
																	  @RequestParam(name = "category_id") String categoryId,
			@RequestParam(name = "sub_category_id") String subCategoryId,
	        @RequestParam(required = false,name = "startPrice") Double startPrice,
			@RequestParam(required = false,name = "toPrice") Double toPrice){
		return new ResponseMessage<>(HttpStatus.OK.value(),
				productService.getProductListByCatOrSubCat(pageable,categoryId, subCategoryId,startPrice, toPrice));
	}


	@GetMapping(UriConstants.SEARCH_BY_BRAND)
	public ResponseMessage<List<Product>> getProductListByBrand(Pageable pageable,
																@RequestParam(name = "brand_id")String brandId,
	@RequestParam(required = false,name = "startPrice") Double startPrice,
	@RequestParam(required = false,name = "toPrice") Double toPrice){
		return new ResponseMessage<>(HttpStatus.OK.value(),productService.getProductListByBrand(pageable,brandId,startPrice,toPrice));
	}

	/**
	 * This method provides an API for Get all Product list. This method accept Post
	 * Http request with pageSize, sortOrder, sortValue, globalSearch and return PageImpl object.
	 *
	 * @param globaleSearch {@link String}
	 * 
	 * @return {@link PageImpl}
	 * @throws ParseException
	 * 
	 * @see ProductService
	 */
	@PostMapping(UriConstants.GLOBAL_SEARCH)
	public ResponseMessage<PageImpl<Product>> getProductList(Pageable pageable,
			@RequestParam(required = false, name = "globalSearch") String globaleSearch) throws ParseException {
		return new ResponseMessage<>(HttpStatus.OK.value(), productService.getGlobalSearchList(
				pageable, globaleSearch));
	}

	//-------------------------Delete Product--------------------------------------

	@DeleteMapping(UriConstants.DELETE)
	public ResponseMessage<List> deleteProduct(@Valid @RequestParam(name = "id")long id){
	this.productService.deleteProductById(id);
		return new ResponseMessage<>(HttpStatus.OK.value(),MsgConstants.CARD_PRODUCT_DELETE);
	}

}
