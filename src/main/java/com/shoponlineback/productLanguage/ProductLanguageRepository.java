package com.shoponlineback.productLanguage;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductLanguageRepository extends CrudRepository<ProductLanguage, Long> {


}
