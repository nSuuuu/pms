package com.niit.service;

import com.niit.entity.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Integer courseId, Integer userId, Order.PayMethod payMethod);
    Order payOrder(Integer orderId);
    Order cancelOrder(Integer orderId, String reason);
    Order refundOrder(Integer orderId, String reason);
    Order getOrderById(Integer orderId);
    List<Order> getOrdersByUser(Integer userId);
    List<Order> getAllOrders();
} 