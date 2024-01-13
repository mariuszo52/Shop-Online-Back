package com.shoponlineback.order.orderManagement;

import com.shoponlineback.order.Order;
import com.shoponlineback.order.OrderStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderManagementRepository extends CrudRepository<Order, Long> {
    List<Order> findOrdersByUserId(long userId);
    List<Order> findOrdersByOrderStatus(OrderStatus orderStatus);
}
