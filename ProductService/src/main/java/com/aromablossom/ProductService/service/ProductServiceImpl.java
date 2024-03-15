package com.aromablossom.ProductService.service;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.aromablossom.ProductService.entity.Product;
import com.aromablossom.ProductService.exception.ProductServiceCustomException;
import com.aromablossom.ProductService.model.ProductRequest;
import com.aromablossom.ProductService.model.ProductResponse;
import com.aromablossom.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

  @Autowired private ProductRepository productRepository;

  @Override
  public long addProduct(ProductRequest productRequest) {
    log.info("Adding product..");

    Product product =
        Product.builder()
            .productName(productRequest.getName())
            .quantity(productRequest.getQuantity())
            .price(productRequest.getPrice())
            .build();
    productRepository.save(product);

    log.info("Product Created");
    return product.getProductId();
  }

  @Override
  public ProductResponse getProductById(long productId) {
    log.info("Get the Product for productid: {}", productId);

    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(
                () ->
                    new ProductServiceCustomException(
                        "Product with given id not found", "Product notfound"));

    ProductResponse productResponse = new ProductResponse();

    copyProperties(product, productResponse);

    return productResponse;
  }

  @Override
  public void increaseQuantity(long productId, long quantity) {
    log.info("Increase Quantity {} for Id: {}", quantity, productId);
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(
                () ->
                    new ProductServiceCustomException(
                        "Product with given Id not found", "PRODUCT_NOT_FOUND"));
    product.setQuantity(product.getQuantity() + quantity);
    productRepository.save(product);
    log.info("Product Quantity updated Successfully!!");
    return;
  }

  @Override
  public void reduceQuantity(long productId, long quantity) {
    log.info("Reduce Quantity {} for Id: {}", quantity, productId);
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(
                () ->
                    new ProductServiceCustomException(
                        "Product with given Id not found", "PRODUCT_NOT_FOUND"));
    if (product.getQuantity() < quantity) {
      throw new ProductServiceCustomException(
          "Product doesn't have sufficient Quantity", "INSUFFICIENT_QUANTITY");
    }
    product.setQuantity(product.getQuantity() - quantity);
    productRepository.save(product);
    log.info("Product Quantity updated Successfully!!");
    return;
  }
}
