package com.shoponlineback.productGenres;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductGenresRepository extends CrudRepository<ProductGenres, Long> {
    void deleteAllByProductId(Long id);
}
