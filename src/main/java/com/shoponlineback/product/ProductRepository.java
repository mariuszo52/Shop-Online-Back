package com.shoponlineback.product;

import com.shoponlineback.language.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.nio.file.LinkOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByName(String name);
    Boolean existsByName(String name);
    List<Product> findAllByPlatform_Device(String deviceName);
    List<Product> findAllByPlatform_Name(String platformName);
    List<Product> findAllByAddedDateIsBefore(LocalDate localDate);
}
