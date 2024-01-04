package com.shoponlineback.user;

import com.shoponlineback.order.OrderDto;
import com.shoponlineback.userInfo.UserInfoDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "\\S+", message = "Username needs to have 1-30 characters without blank spaces.")
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 1, max = 30)
    private String userRole;
    @NotNull
    private UserInfoDto userInfo;
    @NotNull
    private Boolean isEnabled;
    @Size(min = 40, max = 40)
    private String activationToken;
    private List<OrderDto> order;
}
