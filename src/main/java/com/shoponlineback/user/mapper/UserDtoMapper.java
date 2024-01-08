package com.shoponlineback.user.mapper;

import com.shoponlineback.order.OrderDto;
import com.shoponlineback.order.OrderDtoMapper;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserDto;
import com.shoponlineback.userInfo.UserInfo;
import com.shoponlineback.userInfo.UserInfoDtoMapper;

import java.util.List;

public class UserDtoMapper {

    public static UserDto map(User user){
        List<OrderDto> orderDtoList = user.getOrder().stream()
                .map(OrderDtoMapper::map)
                .toList();
        return UserDto.builder()
                .id(user.getId())
                .userInfo(UserInfoDtoMapper.map(user.getUserInfo()))
                .activationToken(user.getActivationToken())
                .email(user.getEmail())
                .type(user.getType().toString())
                .isEnabled(user.getIsEnabled())
                .userRole(user.getUserRole().getName())
                .username(user.getUsername())
                .order(orderDtoList).build();
    }
}
