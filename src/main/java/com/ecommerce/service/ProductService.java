package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Product Service - Business logic for products
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get product by ID
     */
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    /**
     * Search products by name
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Get products in price range
     */
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Get available products
     */
    public List<Product> getAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    /**
     * Create new product
     */
    public Product createProduct(Product product) {
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        return productRepository.save(product);
    }

    /**
     * Update existing product
     */
    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        
        if (!optionalProduct.isPresent()) {
            return null;
        }

        Product product = optionalProduct.get();
        
        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getQuantity() != null) {
            product.setQuantity(productDetails.getQuantity());
        }
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }

        product.setUpdatedAt(new Date());
        return productRepository.save(product);
    }

    /**
     * Delete product
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Check product availability
     */
    public boolean isAvailable(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        return product != null && product.getQuantity() >= quantity;
    }
}
