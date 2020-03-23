package com.product.repository;

import com.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends BaseRepository<Product, UUID> {

    Optional<Product> findByid(UUID id);

}