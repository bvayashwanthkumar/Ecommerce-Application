package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService
 */
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrderById() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setTotalAmount(100.0);
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrderById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(100.0, result.getTotalAmount());
    }

    @Test
    void testCreateOrder() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setTotalAmount(500.0);
        savedOrder.setStatus(Order.OrderStatus.PENDING);
        savedOrder.setUser(user);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = orderService.createOrder(userId, 500.0, "Test order");

        // Assert
        assertNotNull(result);
        assertEquals(Order.OrderStatus.PENDING, result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCancelOrder() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.OrderStatus.PENDING);
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        boolean result = orderService.cancelOrder(orderId);

        // Assert
        assertTrue(result);
        assertEquals(Order.OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testCancelOrderNotPending() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Order.OrderStatus.SHIPPED);
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        boolean result = orderService.cancelOrder(orderId);

        // Assert
        assertFalse(result);
    }
}
