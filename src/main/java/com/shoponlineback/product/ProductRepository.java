package com.shoponlineback.product;

import com.shoponlineback.language.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.LinkOption;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAllByPlatform_Device(String deviceName);
    List<Product> findAllByPlatform_Name(String platformName);
}
