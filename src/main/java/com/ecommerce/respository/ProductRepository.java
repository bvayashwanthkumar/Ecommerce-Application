package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Product Repository - Database access for products
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by name (case-insensitive)
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find products in price range
     */
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * Find products by category
     */
    List<Product> findByCategory(String category);

    /**
     * Find products with quantity greater than zero
     */
    @Query("SELECT p FROM Product p WHERE p.quantity > 0")
    List<Product> findAvailableProducts();
}
