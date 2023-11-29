package com.shoponlineback.user.mapper;

import com.shoponlineback.user.User;
import com.shoponlineback.user.dto.UserAccountInfoDto;

public class UserAccountInfoDtoMapper {

    public static UserAccountInfoDto map(User user){
        return UserAccountInfoDto.builder()
                .email(user.getEmail())
                .name(user.getUserInfo().getName())
                .lastName(user.getUserInfo().getLastName())
                .address(user.getUserInfo().getAddress())
                .city(user.getUserInfo().getCity())
                .country(user.getUserInfo().getCountry())
                .phoneNumber(user.getUserInfo().getPhoneNumber())
                .postalCode(user.getUserInfo().getPostalCode())
                .build();
    }
}
