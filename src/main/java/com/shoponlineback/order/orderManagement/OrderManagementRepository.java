package com.shoponlineback.order.orderManagement;

import com.shoponlineback.order.Order;
import com.shoponlineback.order.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderManagementRepository extends CrudRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {
    Page<Order> findOrdersByUserId(long userId, Pageable pageable);
    Page<Order> findOrdersByOrderStatus(OrderStatus orderStatus, Pageable pageable);
}
