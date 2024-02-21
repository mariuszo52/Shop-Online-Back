package com.shoponlineback.product;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPagingRepository extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {


}