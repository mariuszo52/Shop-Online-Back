package com.shoponlineback.user;

import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.user.dto.UserAccountInfoDto;
import com.shoponlineback.user.mapper.UserAccountInfoDtoMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static User getLoggedUser(){
        return Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getDetails())
                .orElseThrow(() -> new RuntimeException("User is not logged."));

    }

    public  String generateUsername(String email) {
        String username = email.substring(0, email.indexOf("@"));
        Random random = new Random();
        int randomInt = random.nextInt(9999);
        while(userRepository.existsUserByUsername(username)){
            username = username + randomInt;
        }
        return username;
    }

     UserAccountInfoDto getLoggedUserAccountInfo() {
         return UserAccountInfoDtoMapper.map(getLoggedUser());

     }
}
