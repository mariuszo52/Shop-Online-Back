package com.shoponlineback.register;

import com.shoponlineback.cart.Cart;
import com.shoponlineback.email.EmailService;
import com.shoponlineback.exceptions.user.BadRegistrationDataException;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.shippingAddress.ShippingAddress;
import com.shoponlineback.shippingAddress.ShippingAddressRepository;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserRepository;
import com.shoponlineback.user.dto.UserRegisterDto;
import com.shoponlineback.userInfo.UserInfo;
import com.shoponlineback.userInfo.UserInfoRepository;
import com.shoponlineback.userRole.UserRole;
import com.shoponlineback.userRole.UserRoleRepository;
import jakarta.mail.MessagingException;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service

public class RegisterService {
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final EmailService emailService;
    private final ShippingAddressRepository shippingAddressRepository;
    private final static Random random = new Random();
    private final static String RANDOM_TOKEN_CHARS = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
    private final static String RANDOM_PASSWORD_CHARS= "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*()_+={}:";

    RegisterService(UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder,
                    UserRepository userRepository, UserInfoRepository userInfoRepository,
                    EmailService emailService, ShippingAddressRepository shippingAddressRepository) {
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.emailService = emailService;
        this.shippingAddressRepository = shippingAddressRepository;
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
            ShippingAddress shippingAddress = shippingAddressRepository.save(new ShippingAddress());
            UserInfo userInfo = new UserInfo(userRegisterDto.getName(), userRegisterDto.getLastName(), shippingAddress);
            UserInfo userInfoEntity = userInfoRepository.save(userInfo);
            UserRole userRole = userRoleRepository.findUserRoleByName("USER")
                    .orElseThrow(UserRoleNotFoundException::new);
            String token = generateActivationToken();
            User user = User.builder()
                    .isEnabled(false)
                    .username(userRegisterDto.getUsername())
                    .email(userRegisterDto.getEmail())
                    .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                    .userRole(userRole)
                    .userInfo(userInfoEntity)
                    .cart(new Cart())
                    .activationToken(token).build();
            userRepository.save(user);
            try {
                emailService.sendActivationLink(generateActivationLink(token), user.getEmail());

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String generateActivationToken() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            int randomCharIndex = random.nextInt(RANDOM_TOKEN_CHARS.length() - 1);
            stringBuilder.append(RANDOM_TOKEN_CHARS.charAt(randomCharIndex));
        }
        return stringBuilder.toString();
    }

    private String generateActivationLink(String token) {
        return "http://localhost:8080/register/activate?activationToken=" + token;
    }

    @Transactional
    public void activateAccount(String activationToken) {
        User user = userRepository.findUserByActivationToken(activationToken)
                .orElseThrow(() -> new RuntimeException("Activation failed."));
        user.setIsEnabled(true);
    }
    String suggestStrongPassword(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int randomCharIndex = random.nextInt(RANDOM_PASSWORD_CHARS.length() - 1);
            stringBuilder.append(RANDOM_PASSWORD_CHARS.charAt(randomCharIndex));
        }
        if(stringBuilder.toString().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")) {
            return stringBuilder.toString();
        }else {
           return suggestStrongPassword();
        }
    }
}

