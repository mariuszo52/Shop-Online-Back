package com.shoponlineback.user.mapper;

import com.shoponlineback.user.User;
import com.shoponlineback.user.dto.UserShippingInfoDto;

public class UserAccountInfoDtoMapper {

    public static UserShippingInfoDto map(User user){
        return UserShippingInfoDto.builder()
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
