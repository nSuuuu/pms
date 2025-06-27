package com.niit.repository;

import com.niit.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);
    List<Order> findByCourseId(Integer courseId);
    List<Order> findByCourse_TeacherId(Integer teacherId);

    List<Order> findByUserIdAndStatus(Integer userId, Order.OrderStatus status);

} 