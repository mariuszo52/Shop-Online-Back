package com.shoponlineback.cartProducts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends CrudRepository<CartProduct, Long> {
    Optional<CartProduct> findByCart_IdAndProduct_Id(Long cartId, Long productId);
    List<CartProduct> findCartProductByCart_id(Long cartId);
    Boolean existsByCart_IdAndProduct_Id(Long cartId, Long productId);
    void deleteAllByCart_Id(Long cartId);
    void deleteCartProductByCartIdAndProduct_id(Long cartId, Long productId);
}
