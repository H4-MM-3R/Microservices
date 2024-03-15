package com.aromablossom.ProductService.service;

import com.aromablossom.ProductService.model.ProductRequest;
import com.aromablossom.ProductService.model.ProductResponse;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);

    void increaseQuantity(long productId, long quantity);
    
}
