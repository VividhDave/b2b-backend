package com.divergent.mahavikreta.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.divergent.mahavikreta.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select product from Product product Order by product.id DESC")
    Page<Product> findAllProduct(Pageable pageable);

    @Query("select p from Product p where lower(p.productName) like %:searchTerm% or lower(p.productCode) like %:searchTerm% or lower(p.displayName) like %:searchTerm% or lower(p.brand.name) like %:searchTerm% or lower(p.category.name) like %:searchTerm% or lower(p.subCategory.name) like %:searchTerm%")
    Page<Product> globalSearch(Pageable pageable, @Param("searchTerm")String searchTerm);

    Product findProductById(Long productId);

    @Query("select product from Product product where product.productName like :productName%")
    List<Product> getProductListByProductName(@Param("productName") String productName);

    Product findProductByProductCode(String code);

    void deleteById(Long id);

    @Query("select p from Product p where p.discountPrice between :startPrice and :toPrice and p.subCategory.id =:subcategory and p.category.id=:category")
    Page<Product> getProductList(@Param("startPrice")Double startPrice,@Param("toPrice")Double toPrice,Pageable pageable,@Param("subcategory")Long subCategoryId,@Param("category")Long categoryId);

    @Query("select p from Product p where p.discountPrice between :startPrice and :toPrice and p.brand.id =:brand")
    Page<Product> getProductListByBrand(@Param("startPrice")Double startPrice,@Param("toPrice")Double toPrice,Pageable pageable,@Param("brand")Long brandId);
}
