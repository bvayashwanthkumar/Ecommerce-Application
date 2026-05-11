package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService
 */
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");
        
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<Product> products = productService.getAllProducts();

        // Assert
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(productId, result.getId());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductByIdNotFound() {
        // Arrange
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Product result = productService.getProductById(productId);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testCreateProduct() {
        // Arrange
        Product product = new Product();
        product.setName("New Product");
        product.setPrice(99.99);
        product.setQuantity(10);
        
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("New Product");
        savedProduct.setPrice(99.99);
        savedProduct.setQuantity(10);
        
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(productId);

        // Act
        boolean result = productService.deleteProduct(productId);

        // Assert
        assertTrue(result);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testIsAvailable() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setQuantity(10);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        boolean result = productService.isAvailable(productId, 5);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsNotAvailable() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setQuantity(3);
        
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        boolean result = productService.isAvailable(productId, 5);

        // Assert
        assertFalse(result);
    }
}
