package com.shoponlineback.userProducts;

import com.shoponlineback.product.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteProductsRepository extends CrudRepository<UserProducts, Long> {
    List<UserProducts> findAllByUser_Id(Long userId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
    void deleteAllByUserId(Long userId);
}
