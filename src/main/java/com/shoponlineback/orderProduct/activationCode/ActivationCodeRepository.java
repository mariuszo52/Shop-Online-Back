package com.shoponlineback.orderProduct.activationCode;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivationCodeRepository extends CrudRepository<ActivationCode, Long> {
    List<ActivationCode> findAllByOrderProductId(Long orderProductId);
}
