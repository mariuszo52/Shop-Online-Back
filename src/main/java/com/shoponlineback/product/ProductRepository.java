package com.shoponlineback.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.LinkOption;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
