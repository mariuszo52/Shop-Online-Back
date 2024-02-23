package com.shoponlineback.product.productManagement;

import com.shoponlineback.product.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ProductManagementRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long> {
    Optional<Product> getProductByName(String name);
}
