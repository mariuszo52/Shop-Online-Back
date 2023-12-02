package com.shoponlineback.user.mapper;

import com.shoponlineback.user.User;
import com.shoponlineback.user.dto.UserAccountInfoDto;

public class UserAccountInfoDtoMapper {

    public static UserAccountInfoDto map(User user){
        return UserAccountInfoDto.builder()
                .email(user.getEmail())
                .name(user.getUserInfo().getName())
                .lastName(user.getUserInfo().getLastName())
                .address(user.getUserInfo().getShippingAddress().getAddress())
                .city(user.getUserInfo().getShippingAddress().getCity())
                .country(user.getUserInfo().getShippingAddress().getCountry())
                .phoneNumber(user.getUserInfo().getShippingAddress().getPhoneNumber())
                .postalCode(user.getUserInfo().getShippingAddress().getPostalCode())
                .build();
    }
}
