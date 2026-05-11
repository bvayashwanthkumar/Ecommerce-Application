package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Order Service - Business logic for orders
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    /**
     * Get orders by user
     */
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * Create new order
     */
    public Order createOrder(Long userId, Double totalAmount, String notes) {
        Optional<User> optionalUser = userRepository.findById(userId);
        
        if (!optionalUser.isPresent()) {
            return null; // User not found
        }

        Order order = new Order();
        order.setUser(optionalUser.get());
        order.setTotalAmount(totalAmount);
        order.setNotes(notes);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        return orderRepository.save(order);
    }

    /**
     * Update order status
     */
    public Order updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        
        if (!optionalOrder.isPresent()) {
            return null;
        }

        Order order = optionalOrder.get();
        order.setStatus(newStatus);
        order.setUpdatedAt(new Date());

        return orderRepository.save(order);
    }

    /**
     * Cancel order (only if pending)
     */
    public boolean cancelOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        
        if (!optionalOrder.isPresent()) {
            return false;
        }

        Order order = optionalOrder.get();
        
        if (order.getStatus() == Order.OrderStatus.PENDING) {
            order.setStatus(Order.OrderStatus.CANCELLED);
            order.setUpdatedAt(new Date());
            orderRepository.save(order);
            return true;
        }

        return false;
    }

    /**
     * Get high-value orders
     */
    public List<Order> getHighValueOrders(Double minAmount) {
        return orderRepository.findByTotalAmountGreaterThan(minAmount);
    }
}
