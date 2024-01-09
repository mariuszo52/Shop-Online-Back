package com.shoponlineback.product.productManagement;

import com.shoponlineback.product.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductManagementRepository extends CrudRepository<Product, Long> {
}
