package com.shoponlineback.user;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}