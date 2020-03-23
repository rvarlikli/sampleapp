package com.product.service;

import com.product.domain.Product;
import com.product.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.product.repository.ProductRepository;
import com.product.util.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        if (product.getId() == null)
            product.setId(UUID.randomUUID());
        return productRepository.save(product);
    }

    public Product findProductById(UUID productId) {
        logger.info("Retrieving product {}", productId);
        Product product = productRepository.findByid(productId).orElseThrow(() -> new ApplicationException("Not found", ErrorCode.NOT_FOUND));
        logger.info("Loaded product {} - {}.", productId, product.getName());
        return product;
    }

    public List<Product> getAllProducts() {
        logger.info("Retrieving products ...");
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        logger.info("Retrieving product {}", product.getId());
        if (productRepository.findByid(product.getId()).isPresent()) {
            logger.info("Updating product {}", product.getId());
            return  productRepository.save(product);
        } else {
            logger.info("Product not found {}", product.getId());
            return product;
        }
    }

    public void deleteProduct(UUID productId) {
        logger.info("Retrieving product {}", productId);
        productRepository.findByid(productId).ifPresent(product -> {
            logger.info("Deleting product {}", productId);
            productRepository.delete(product);
        });
    }
}
