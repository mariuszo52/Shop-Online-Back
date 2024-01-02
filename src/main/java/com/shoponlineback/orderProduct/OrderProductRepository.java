package com.shoponlineback.orderProduct;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends CrudRepository<OrderProduct,Long> {
    List<OrderProduct> findOrderProductsByOrderIdIn(List<Long> orderIds);
    List<OrderProduct> findOrderProductsByOrderId(Long orderId);
}
