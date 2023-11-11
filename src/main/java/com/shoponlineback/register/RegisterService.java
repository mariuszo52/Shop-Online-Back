package com.shoponlineback.register;

import com.shoponlineback.exceptions.user.BadRegistrationDataException;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserRegisterDto;
import com.shoponlineback.userRole.UserRole;
import com.shoponlineback.userRole.UserRoleRepository;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class RegisterService {
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    RegisterService(UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    void registerUser(@NonNull UserRegisterDto userRegisterDto) {
        if (userRepository.existsUserByEmail(userRegisterDto.getEmail())) {
            throw new BadRegistrationDataException("Email is already exist.");
        } else if (!userRegisterDto.getEmail().equals(userRegisterDto.getConfirmEmail())) {
            throw new BadRegistrationDataException("Emails not match.");
        } else if (userRepository.existsUserByUsername(userRegisterDto.getUsername())) {
            throw new BadRegistrationDataException("Username is already exist.");
        } else if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            throw new BadRegistrationDataException("Password not match");
        } else {
            UserRole userRole = userRoleRepository.findUserRoleByName("USER")
                    .orElseThrow(UserRoleNotFoundException::new);
            User user = User.builder()
                    .username(userRegisterDto.getUsername())
                    .email(userRegisterDto.getEmail())
                    .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                    .userRole(userRole).build();
            userRepository.save(user);

        }
    }

}
