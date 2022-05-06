package com.divergent.mahavikreta.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.entity.Product;
import com.divergent.mahavikreta.entity.filter.ProductFilter;

public interface ProductService {

    Product save(Map<String, Object> product);

    PageImpl<Product> getList(Pageable pageable,boolean isImage,ProductFilter productFilter) throws ParseException;
    
    PageImpl<Product> getGlobalSearchList(Pageable pageable,String globalSearch) throws ParseException;

    List<Product> getAllRecord();

    Product findById(Long id);

    List<Product> saveProductUsingExcelFile(MultipartFile file);

    Product saveProduct(Product product);

    List<Product> getProductListByProductName(String productName);

    List<Product> getProductListByCatOrSubCat(Pageable pageable, String categoryId, String subCategoryId,Double startPrice,Double toPrice);

    void deleteProductById(long id);

    List<Product> getProductListByBrand(Pageable pageable, String brandId, Double startPrice, Double toPrice);
}
