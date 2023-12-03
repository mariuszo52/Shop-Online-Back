package com.shoponlineback.user;

import com.shoponlineback.exceptions.user.UserNotLoggedInException;
import com.shoponlineback.user.dto.UserAccountInfoDto;
import com.shoponlineback.user.mapper.UserAccountInfoDtoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static User getLoggedUser(){
        return Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getDetails())
                .orElseThrow(UserNotLoggedInException::new);

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
    void deleteStandardAccount(HttpServletRequest request){
        String password = request.getHeader("password");
        if(password != null) {
            User loggedUser = getLoggedUser();
            if (passwordEncoder.matches(password, loggedUser.getPassword())) {
                userRepository.deleteById(loggedUser.getId());
            }else {
                throw new RuntimeException("Wrong password.");
            }
        }else {
            throw new RuntimeException("Empty password.");
        }
    }
    void deleteSocialMediaAccount(){
        userRepository.deleteById(getLoggedUser().getId());
    }

     UserAccountInfoDto getLoggedUserAccountInfo() {
         return UserAccountInfoDtoMapper.map(getLoggedUser());

     }
}
