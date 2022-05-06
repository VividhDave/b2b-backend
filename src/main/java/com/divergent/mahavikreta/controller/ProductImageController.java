package com.divergent.mahavikreta.controller;

import java.util.List;

import javax.validation.Valid;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.exception.GenricException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.constants.UriConstants;
import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.ProductImage;
import com.divergent.mahavikreta.service.ProductImageService;
import com.divergent.mahavikreta.utility.ResponseMessage;

/**
 * This class provide Product Image related API
 * 
 * @see ProductImageService
 * 
 * @author Aakash
 *
 */
@RestController
@RequestMapping(UriConstants.PRODDUCT_IMAGE)
public class ProductImageController {

	@Autowired
	ProductImageService productImageService;

	/**
	 * This method provides an API for save multiple product image. This method
	 * accept Post Http request with request files {@link MultipartFile[]},
	 * productId {@link Product} return {@link List<ProductImage>}.
	 * 
	 * @param files     {@link MultipartFile[]}
	 * @param productId {@link Long} {@link Product}
	 * @return {@link List<Product>}
	 * 
	 * @see ProductImageService
	 */
	@PostMapping(UriConstants.SAVE)
	public ResponseMessage<List<ProductImage>> save(@Valid @RequestParam("files") MultipartFile[] files,
			@Valid @RequestParam("productId") Long productId) {
				MultipartFile file = files[0];
				long size = file.getSize();
				if(size > 1 * 1024 * 1024){
					throw  new GenricException("The size of the image is more then 1 mb");
				}
		return new ResponseMessage<>(HttpStatus.OK.value(), "Product image created successfully",
				productImageService.saveMultiRecord(files, productId));
	}

	/**
	 * This method provides an API for get product attribute list by product id.
	 * This method accept Get Http request with request id {@link Product} return
	 * {@link List<ProductImage>}.
	 * 
	 * @param id {@link Long}
	 * @return {@link List<ProductImage>}
	 * 
	 * @see ProductImageService
	 */
	@SuppressWarnings("JavadocReference")
	@GetMapping(UriConstants.GET_BY_PROD_ID)
	public ResponseMessage<List<ProductImage>> getImageByProductId(@Valid @RequestParam("productId") Long productId) {
		return new ResponseMessage<>(HttpStatus.OK.value(), productImageService.getByProductId(productId));
	}

	@DeleteMapping(UriConstants.DELETE)
	public ResponseMessage<List> deleteImage(@Valid @RequestParam(name = "id")long id){
		this.productImageService.deleteImageById(id);
		return new ResponseMessage<>(HttpStatus.OK.value(), MsgConstants.IMAGE_DELETE);
	}

}
