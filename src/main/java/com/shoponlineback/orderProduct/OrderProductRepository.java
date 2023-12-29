package com.shoponlineback.orderProduct;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends CrudRepository<OrderProduct,Long> {
    List<OrderProduct> findOrderProductsByOrderId(Long id);
}
