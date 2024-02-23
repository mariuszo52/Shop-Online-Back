package com.shoponlineback.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);
    Boolean existsByName(String name);
    Page<Product> findAllByPlatformName(String platformName, Pageable pageable);
    Long countAllByPlatformName(String platformName);
    List<Product> findAllByAddedDateIsBefore(LocalDate localDate);
}
