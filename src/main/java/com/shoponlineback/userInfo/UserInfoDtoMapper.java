package com.shoponlineback.userInfo;


import com.shoponlineback.shippingAddress.ShippingAddressDtoMapper;

public class UserInfoDtoMapper {
    public static UserInfoDto map(UserInfo userInfo){
        return new UserInfoDto(userInfo.getName(), userInfo.getLastName(),
                ShippingAddressDtoMapper.map(userInfo.getShippingAddress()));
    }
}
