package com.shoponlineback.shippingAddress;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static com.shoponlineback.user.UserService.*;

@Service
public class ShippingAddressService {
    private final ShippingAddressRepository shippingAddressRepository;

    public ShippingAddressService(ShippingAddressRepository shippingAddressRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
    }

    @Transactional
    public void editShippingAddress(ShippingAddressDto shippingAddressDto){
        ShippingAddress shippingAddress = shippingAddressRepository.findById(getLoggedUser().getUserInfo().getShippingAddress().getId())
                .orElseThrow(() -> new RuntimeException("User shipping address not found."));
        shippingAddress.setAddress(shippingAddressDto.getAddress());
        shippingAddress.setCity(shippingAddressDto.getCity());
        shippingAddress.setCountry(shippingAddressDto.getCountry());
        shippingAddress.setPostalCode(shippingAddressDto.getPostalCode());
        shippingAddress.setPhoneNumber(shippingAddressDto.getPhoneNumber());
    }
}
