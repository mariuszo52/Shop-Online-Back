package com.shoponlineback.shippingAddress;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingAddressRepository extends CrudRepository<ShippingAddress, Long> {
}
