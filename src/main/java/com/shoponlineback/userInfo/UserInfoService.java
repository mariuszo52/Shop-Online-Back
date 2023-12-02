package com.shoponlineback.userInfo;

import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private final UserRepository userRepository;

    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void editUserInfo(UserInfoDto userInfoDto){
        UserInfo userInfo = userRepository.findById(UserService.getLoggedUser().getId())
                .orElseThrow(UserNotFoundException::new).getUserInfo();
        userInfo.setName(userInfoDto.getName());
        userInfo.setLastName(userInfoDto.getLastName());
    }
}
