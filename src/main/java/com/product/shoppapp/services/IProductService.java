package com.product.shoppapp.services;

import com.product.shoppapp.dtos.ProductDTO;
import com.product.shoppapp.dtos.ProductImageDTO;
import com.product.shoppapp.exceptions.DataNotFoundException;
import com.product.shoppapp.exceptions.InvalidParamException;
import com.product.shoppapp.models.Product;
import com.product.shoppapp.models.ProductImage;
import com.product.shoppapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);

    Product getProductById(Long id) throws Exception;

    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);
}
