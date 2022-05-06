package com.divergent.mahavikreta.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.divergent.mahavikreta.entity.*;
import com.divergent.mahavikreta.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.divergent.mahavikreta.constants.AppConstants;
import com.divergent.mahavikreta.entity.filter.ProductFilter;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.ProductService;
import com.divergent.mahavikreta.utility.AppUtility;
import com.divergent.mahavikreta.utility.ConversionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductAttributeRepository productAttributeRepository;

    @Autowired
    AttributeRepository attributeRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Autowired
    ChilledCategoryRepository chilledCategoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    LogService logService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product save(Map<String, Object> product) {
        Product pr = null;
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productAttribute = (List<Map<String, Object>>) product.get("attribute");
            product.remove("attribute");
            Product prd = (Product) ConversionUtils.convertMapToEntity(product, Product.class);
            pr = productRepository.save(prd);

            List<ProductAttribute> productAttr = new ArrayList<ProductAttribute>();
            for (Iterator<Map<String, Object>> iterator = productAttribute.iterator(); iterator.hasNext(); ) {
                Map<String, Object> obj = iterator.next();
                ProductAttribute productAttribute2 = (ProductAttribute) ConversionUtils.convertMapToEntity(obj,
                        ProductAttribute.class);
                productAttribute2.setProduct(pr);
                productAttr.add(productAttribute2);
            }
            productAttributeRepository.saveAll(productAttr);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            logService.saveErrorLog("Error Saving product details", "ProductController", "save", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
        return pr;
    }

    @Override
    public List<Product> getAllRecord() {
        return productRepository.findAll();
    }

    @Override
    public PageImpl<Product> getList(Pageable pageable, boolean isImage, ProductFilter productFilter)
            throws ParseException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);

            Root<Product> root = criteria.from(Product.class);
            Join<Product, Brand> brandJoin = root.join("brand", JoinType.LEFT);
            Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
            Join<Product, SubCategory> subCategoryJoin = root.join("subCategory", JoinType.LEFT);

            List<Product> workEffortMaps = null;
            long count;
            Page<Product> page;
            if (AppUtility.isEmpty(productFilter)) {
                page = productRepository.findAllProduct(pageable);
                workEffortMaps = (List<Product>) page.getContent();
                count = page.getTotalElements();
            } else {

                List<Predicate> predicates = setAdvanceSeachForProduct(builder, root, brandJoin, categoryJoin,
                        subCategoryJoin, productFilter);

                criteria.where(predicates.toArray(new Predicate[]{})).distinct(true);

                workEffortMaps = entityManager.createQuery(criteria)
                        .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                        .setMaxResults(pageable.getPageSize()).getResultList();
                count = entityManager.createQuery(criteria).getResultList().size();

            }
            if (isImage) {
                workEffortMaps.forEach(e -> {
                    e.setProductImage(null);
                    e.getBrand().setImage(null);
                    e.getCategory().setImage(null);
                    e.getSubCategory().setImage(null);
                    e.getSubCategory().setChilledCategory(null);
                    e.getProductAttribute().forEach(e1 -> e1.setProduct(null));
                });
            } else {
                workEffortMaps.forEach(e -> {
                    e.getBrand().setImage(null);
                    e.getCategory().setImage(null);
                    e.getSubCategory().setImage(null);
                    e.getSubCategory().setChilledCategory(null);
                    e.getProductAttribute().forEach(e1 -> e1.setProduct(null));
                });
            }
            return new PageImpl<>(workEffortMaps, pageable, count);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            logService.saveErrorLog("Error getting product details list", "ProductController", "getList",
                    ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
    }

    @Override
    public Product findById(Long id) {
        if (id == null) {
            throw new GenricException("Please enter valid Id");
        }
        return productRepository.findProductById(id);
    }

    private List<Predicate> setAdvanceSeachForProduct(CriteriaBuilder builder, Root<Product> root,
                                                      Join<Product, Brand> brandJoin, Join<Product, Category> categoryJoin,
                                                      Join<Product, SubCategory> subCategoryJoin, ProductFilter productFilter) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        if (!AppUtility.isEmpty(productFilter.getProductName()))
            predicates.add(builder.like(builder.lower(root.get("productName")),
                    "%" + productFilter.getProductName().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(productFilter.getDisplayName()))
            predicates.add(builder.like(builder.lower(root.get("displayName")),
                    "%" + productFilter.getDisplayName().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(productFilter.getDescription()))
            predicates.add(builder.like(builder.lower(root.get("description")),
                    "%" + productFilter.getDescription().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(productFilter.getPrice()) && productFilter.getPrice() > 0)
            predicates.add(builder.equal(root.get("price"), productFilter.getPrice()));

        if (!AppUtility.isEmpty(productFilter.getDiscountPrice()) && productFilter.getDiscountPrice() > 0)
            predicates.add(builder.equal(root.get("discountPrice"), productFilter.getDiscountPrice()));

        if (!AppUtility.isEmpty(productFilter.getQty()) && productFilter.getQty() > 0)
            predicates.add(builder.equal(root.get("qty"), productFilter.getQty()));

        if (!AppUtility.isEmpty(productFilter.getBrandId()) && productFilter.getBrandId() > 0)
            predicates.add(builder.equal(brandJoin.get("id"), productFilter.getBrandId()));

        if (!AppUtility.isEmpty(productFilter.getCategoryId()) && productFilter.getCategoryId() > 0)
            predicates.add(builder.equal(categoryJoin.get("id"), productFilter.getCategoryId()));

        if (!AppUtility.isEmpty(productFilter.getSubCategoryId()) && productFilter.getSubCategoryId() > 0)
            predicates.add(builder.equal(subCategoryJoin.get("id"), productFilter.getSubCategoryId()));

        if (!AppUtility.isEmpty(productFilter.getSubCategoryName()))
            predicates.add(builder.like(builder.lower(subCategoryJoin.get("name")),
                    "%" + productFilter.getSubCategoryName().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(productFilter.getCategoryName()))
            predicates.add(builder.like(builder.lower(categoryJoin.get("name")),
                    "%" + productFilter.getCategoryName().toLowerCase() + "%"));

        if (!AppUtility.isEmpty(productFilter.getProductPrice())) {
            String[] price = productFilter.getProductPrice().split("-");
            double startPrice = Double.parseDouble(price[0]);
            if (price[1].equalsIgnoreCase("above")) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), startPrice));
            } else {
                double toPrice = Double.parseDouble(price[1]);
                predicates.add(builder.between(root.get("price"), startPrice, toPrice));
            }
        }

        return predicates;
    }

    private static boolean isRowEmpty(Row row) {
        boolean isEmpty = false;
        int i = 0;
        DataFormatter dataFormatter = new DataFormatter();

        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() <= 0) {
                    if(i >= 3) {
                        isEmpty = true;
                        break;
                    }
                    i++;
                }
            }
        }

        return isEmpty;
    }

    public List<Product> getDataFromExcelSheet(MultipartFile multipartFile) {
        List<Product> list = new ArrayList<Product>();
        try {
            File file = ConversionUtils.convertMultiPartToFile(multipartFile);
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            int i = 0;
            List<Map<String, Object>> obj = new ArrayList<Map<String, Object>>();
            while (itr.hasNext()) {
                Row row = itr.next();
                if(isRowEmpty(row)){
                    break;
                }
                Iterator<Cell> cellIterator = row.cellIterator();
                List<ProductAttribute> productAttribute = new ArrayList<>();

                if (i == 0) {
                    obj = getAttributeList(cellIterator);
                } else {
                    int j = 0;
                    Product pr = new Product();
                    pr.setPrice(0);
                    pr.setDiscountPrice(0);
                    while (cellIterator.hasNext()) {
                        Map<String, Object> prodInfo = obj.get(j);
                        Cell cell = cellIterator.next();
                        if ((boolean) prodInfo.get("isAttribute")) {
                            productAttribute = prepareProductAttributeList(productAttribute, cell.getStringCellValue(),
                                    prodInfo);
                        } else {
                            pr = prepareProductObject(cell, prodInfo, pr, i+1);
                        }
                        j++;
                    }
                    Product prod = productRepository.save(pr);
                    list.add(prod);
                    for (ProductAttribute map : productAttribute) {
                        map.setProduct(prod);
                    }
                    productAttributeRepository.saveAll(productAttribute);
                }
                i++;
            }
            wb.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            logService.saveErrorLog("Error in add data from excel sheet", "ProductController", "getDataFromExcelSheet",
                    e.getMessage());
            throw new GenricException(e.getMessage());
        } finally {

        }
        return list;
    }

    public Product prepareProductObject(Cell cell, Map<String, Object> prodInfo, Product product, int rowIndex) {
        String value = (String) prodInfo.get("value");
        try {
            switch (value) {
                case "PRODUCT NAME":
                    product.setProductName(cell.getStringCellValue());
                    break;
                case "MRP":
                    product.setPrice(cell.getNumericCellValue());
                    break;
                case "SHIPPING CHARGE":
                    product.setShippingCharge(cell.getNumericCellValue());
                    break;
                case "SPECIFICATION":
                    product.setSpecification(cell.getStringCellValue());
                    break;
                case "OUR PRICE INCL GST":
                    product.setDiscountPrice(cell.getNumericCellValue());
                    break;
                case "DESCRIPTION":
                    product.setDescription(cell.getStringCellValue());
                    break;
                case "COMPANY NAME":
                    Brand br = getBrand(cell.getStringCellValue());
                    product.setBrand(br);
                    break;
                case "CAPTION":
                    product.setDisplayName(cell.getStringCellValue());
                    break;
                case "TECHNICAL NAME":
                    product.setTechnicalName(cell.getStringCellValue());
                    break;
                case "PACKING":
                    product.setPackaging(cell.getStringCellValue());
                    break;
                case "PRODUCT CODE":
                    Product check = productRepository.findProductByProductCode(cell.getStringCellValue());
                    if (!AppUtility.isEmpty(check)) {
                        throw new GenricException("Product Already Exist product code:- " + cell.getStringCellValue());
                    }
                    product.setProductCode(cell.getStringCellValue());
                    break;
                case "HSN CODE":
                    product.setHsnCode(cell.getNumericCellValue());
                    break;
                case "AVAILABLE STOCK":
                    product.setQty((int) cell.getNumericCellValue());
                    break;
                case "CATEGORY NAME":
                    Category cat = getCategory(cell.getStringCellValue());
                    product.setCategory(cat);
                    break;
                case "SUB CATEGORY NAME":
                    SubCategory subCat = getSubCategory(cell.getStringCellValue());
                    product.setSubCategory(subCat);
                    break;
                case "MARGIN":
                    product.setMargin((int) cell.getNumericCellValue());
                    break;
                case "CHILD CATEGORY":
                    if (!AppUtility.isEmpty(cell.getStringCellValue())){
                        ChilledCategory chilledCategory = getChildCategory(cell.getStringCellValue());
                        product.setChilledCategory(chilledCategory);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception ex ) {
            log.error(ex.getMessage());
            logService.saveErrorLog("Error in Prepare Product Object for saving data from excel sheet",
                    "ProductController", "prepareProductObject", "Getting exception for "+value+" at row index "+rowIndex+ ": " + ex.getMessage());
            throw new GenricException("Getting exception for "+value+" at row index "+rowIndex+ ": " + ex.getMessage());
        }

        return product;
    }

    public Brand getBrand(String brandName) {
        try {
            Brand br = brandRepository.findBrandByName(brandName.trim());
            if (AppUtility.isEmpty(br)) {
                Brand brand = new Brand();
                brand.setName(brandName);
                br = brandRepository.save(brand);
            }
            return br;
        } catch (Exception ex) {
            log.error("Error in Get and save new Brand for saving data from excel sheet" + ex.getMessage());
            logService.saveErrorLog("Error in Get and save new Brand for saving data from excel sheet",
                    "ProductController", "getBrand", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
    }

    public Category getCategory(String catName) {
        Category cat = categoryRepository.findCategoryByName(catName.trim());
        if (AppUtility.isEmpty(cat)) {
            throw new GenricException("Category " + catName + " not exist please create category :- ");
        }
        return cat;
    }

    public SubCategory getSubCategory(String subCatName) {
        SubCategory subCat = subCategoryRepository.findSubCategoryByName(subCatName.trim());
        if (AppUtility.isEmpty(subCat)) {
            throw new GenricException("Sub Category " + subCatName + " not exist please create Sub Category :- ");
        }
        return subCat;
    }

    public ChilledCategory getChildCategory(String childCatName) {
        ChilledCategory childCat = chilledCategoryRepository.findChilledCategoryByName(childCatName.trim());
        if (AppUtility.isEmpty(childCat)) {
            throw new GenricException("Child Category " + childCatName + " not exist please create Child Category :- ");
        }
        return childCat;
    }

    public List<ProductAttribute> prepareProductAttributeList(List<ProductAttribute> productAttribute, String value,
                                                              Map<String, Object> info) {
        try {
            ProductAttribute pr = new ProductAttribute();
            Attribute attribute = (Attribute) info.get("value");
            pr.setAttribute(attribute);
            pr.setValue(value);
            productAttribute.add(pr);
            return productAttribute;
        } catch (Exception ex) {
            log.error("Error in Prepare Product Attribute List for saving data from excel sheet" + ex.getMessage());
            logService.saveErrorLog("Error in Prepare Product Attribute List for saving data from excel sheet",
                    "ProductController", "prepareProductAttributeList", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
    }

    public List<Map<String, Object>> getAttributeList(Iterator<Cell> cellIterator) {
        List<Map<String, Object>> attributeList = new ArrayList<>();
        try {
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case STRING:
                        Map<String, Object> attr = getAttribute(cell.getStringCellValue());
                        attributeList.add(attr);
                        break;
                    case NUMERIC:
                        attr = getAttribute(cell.getNumericCellValue() + "");
                        attributeList.add(attr);
                        break;
                    case BOOLEAN:
                        attr = getAttribute(cell.getBooleanCellValue() + "");
                        attributeList.add(attr);
                        break;
                    default:
                }

            }
        } catch (Exception ex) {
            log.error("Error in Get Attribute List for saving data from excel sheet" + ex.getMessage());
            logService.saveErrorLog("Error in Get Attribute List for saving data from excel sheet", "ProductController",
                    "getAttributeList", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
        return attributeList;
    }

    public Map<String, Object> getAttribute(String value) {
        Map<String, Object> prodInfo = new HashMap<>();
        try {
            String values = AppUtility.compareValue(value, AppConstants.PRODUCT_INFO);
            if (!AppUtility.isEmpty(values)) {
                prodInfo.put("isAttribute", false);
                prodInfo.put("value", value);
                return prodInfo;
            }
            Attribute attribute = attributeRepository.findAttributeByName(value);
            if (AppUtility.isEmpty(attribute)) {
                Attribute attr = new Attribute();
                attr.setName(value);
                attr.setType("string");
                String key = value.replaceAll("\\s+", "");
                attr.setKey(key);
                attribute = attributeRepository.save(attr);
                prodInfo.put("isAttribute", true);
                prodInfo.put("value", attribute);
            } else {
                prodInfo.put("isAttribute", true);
                prodInfo.put("value", attribute);
            }
        } catch (Exception ex) {
            log.error("Error in Get Attribute List for saving data from excel sheet" + ex.getMessage());
            logService.saveErrorLog("Error in Get Attribute List for saving data from excel sheet", "ProductController",
                    "getAttributeList", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
        return prodInfo;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Product> saveProductUsingExcelFile(MultipartFile file) {
        return getDataFromExcelSheet(file);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getProductListByProductName(String productName) {
        return productRepository.getProductListByProductName(productName);
    }


    @Override
    public List<Product> getProductListByCatOrSubCat(Pageable pageable,String categoryId, String subCategoryId,Double startPrice,Double toPrice) {
        String query = "select product from Product product where 1=1 ";
        if (categoryId != null && categoryId.length() != 0 && !categoryId.equals("0") && !categoryId.equals("")) {
            query += " and category_id in (" + categoryId + ")";
        }
        if (subCategoryId != null && subCategoryId.length() != 0 && !subCategoryId.equals("0")
                && !subCategoryId.equals("")) {
            query += " and sub_category_id in (" + subCategoryId + ")";
        }
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, Product.class);
        List<Product> workEffortMaps = null;
        long count = 0;
        Page<Product> page;
        try {
            if (AppUtility.isEmpty(startPrice)) {
//                page = productRepository.findAllProduct(pageable);
//                workEffortMaps = (List<Product>) page.getContent();
//                count = page.getTotalElements();
                 workEffortMaps = typedQuery.getResultList();

            } else {
                workEffortMaps = productRepository.getProductList(startPrice, toPrice, pageable,Long.parseLong(subCategoryId),Long.parseLong(categoryId)).getContent();
            }
        } catch (Exception ex) {
            log.error("Error in Geting the product List" + ex.getMessage());
            logService.saveErrorLog("Error in Geting get product List", "ProductController",
                    "getProductList", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
        return workEffortMaps;
    }
//-----------------Delete Product-------------------------------
    @Override
    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductListByBrand(Pageable pageable, String brandId, Double startPrice, Double toPrice) {
        String query = "select product from Product product where 1=1 ";
        if (brandId != null && brandId.length() != 0 && !brandId.equals("0") && !brandId.equals("")) {
            query += " and brand_id in (" + brandId + ")";
        }
        TypedQuery<Product> typedQuery = entityManager.createQuery(query, Product.class);
        List<Product> workEffortMaps = null;
        long count = 0;
        Page<Product> page;
        try {
            if (AppUtility.isEmpty(startPrice)) {
//                page = productRepository.findAllProduct(pageable);
//                workEffortMaps = (List<Product>) page.getContent();
//                count = page.getTotalElements();
                workEffortMaps = typedQuery.getResultList();

            } else {
                workEffortMaps = productRepository.getProductListByBrand(startPrice, toPrice, pageable,Long.parseLong(brandId)).getContent();
            }
        } catch (Exception ex) {
            log.error("Error in Geting the product List" + ex.getMessage());
            logService.saveErrorLog("Error in Geting get product List", "ProductController",
                    "getProductList", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }
        return workEffortMaps;
    }

    @Override
    public PageImpl<Product> getGlobalSearchList(Pageable pageable, String globalSearch) throws ParseException {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        Root<Product> root = criteria.from(Product.class);
        Join<Product, Brand> brandJoin = root.join("brand", JoinType.LEFT);
        Join<Product, Category> categoryJoin = root.join("category", JoinType.LEFT);
        Join<Product, SubCategory> subCategoryJoin = root.join("subCategory", JoinType.LEFT);
        List<Product> workEffortMaps = null;
        long count = 0;
        Page<Product> page;
        try {
            if (AppUtility.isEmpty(globalSearch)) {
                page = productRepository.findAllProduct(pageable);
                workEffortMaps = (List<Product>) page.getContent();
                count = page.getTotalElements();
            } else {
//                List<Predicate> predicates = setAdvanceGlobalSeachForProduct(builder, root, brandJoin, categoryJoin,
//                        subCategoryJoin, globalSearch);
//                criteria.where(predicates.toArray(new Predicate[]{})).distinct(true);
//                workEffortMaps = entityManager.createQuery(criteria)
//                        .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
//                        .setMaxResults(pageable.getPageSize()).getResultList();
//                count = entityManager.createQuery(criteria).getResultList().size();
                page =  productRepository.globalSearch(pageable, globalSearch);
            }
        } catch (Exception ex) {
            log.error("Error in Geting get Global Search product List" + ex.getMessage());
            logService.saveErrorLog("Error in Geting get Global Search product List", "ProductController",
                    "getGlobalSearchList", ex.getMessage());
            throw new GenricException(ex.getMessage());
        }

        return new PageImpl<>(page.getContent(), pageable, count);
    }

    private List<Predicate> setAdvanceGlobalSeachForProduct(CriteriaBuilder builder, Root<Product> root,
                                                            Join<Product, Brand> brandJoin, Join<Product, Category> categoryJoin,
                                                            Join<Product, SubCategory> subCategoryJoin, String globalSearch) throws ParseException {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.like(builder.lower(root.get("productName")), "%" + globalSearch.toLowerCase() + "%"));

        predicates.add(builder.like(builder.lower(brandJoin.get("name")), "%" + globalSearch.toLowerCase() + "%"));

        predicates.add(builder.like(builder.lower(categoryJoin.get("name")), "%" + globalSearch.toLowerCase() + "%"));

        predicates.add(builder.like(builder.lower(subCategoryJoin.get("name")), "%" + globalSearch.toLowerCase() + "%"));

        return predicates;
    }

}
