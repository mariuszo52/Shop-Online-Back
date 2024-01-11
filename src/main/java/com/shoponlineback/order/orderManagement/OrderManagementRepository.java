package com.shoponlineback.order.orderManagement;

import com.shoponlineback.order.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderManagementRepository extends CrudRepository<Order, Long> {
}
