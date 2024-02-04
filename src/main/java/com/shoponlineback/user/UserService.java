package com.shoponlineback.user;

import com.shoponlineback.exceptions.user.UserNotLoggedInException;
import com.shoponlineback.order.OrderRepository;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.orderProduct.activationCode.ActivationCodeRepository;
import com.shoponlineback.user.dto.UserShippingInfoDto;
import com.shoponlineback.user.mapper.UserAccountInfoDtoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ActivationCodeRepository activationCodeRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       OrderRepository orderRepository, OrderProductRepository orderProductRepository,
                       ActivationCodeRepository activationCodeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.activationCodeRepository = activationCodeRepository;
    }

    public static User getLoggedUser() {
        return Optional.of((User) SecurityContextHolder.getContext().getAuthentication().getDetails())
                .orElseThrow(UserNotLoggedInException::new);

    }

    public String generateUsername(String email) {
        String username = email.substring(0, email.indexOf("@"));
        Random random = new Random();
        int randomInt = random.nextInt(9999);
        while (userRepository.existsUserByUsername(username)) {
            username = username + randomInt;
        }
        return username;
    }
    @Transactional
    public void deleteStandardUserOrderProducts(HttpServletRequest request) {
        String password = request.getHeader("password");
        if (password != null) {
            User loggedUser = getLoggedUser();
            if (passwordEncoder.matches(password, loggedUser.getPassword())) {
                orderRepository.findAllByUser_Id(loggedUser.getId()).forEach(order -> {
                    orderProductRepository.findOrderProductsByOrderId(order.getId()).forEach(orderProduct -> {
                        activationCodeRepository.deleteAllByOrderProductId(orderProduct.getId());
                        orderProductRepository.delete(orderProduct);
                    });
                });

            } else {
                throw new RuntimeException("Wrong password.");
            }
        } else {
            throw new RuntimeException("Empty password.");
        }
    }
    @Transactional
    public void deleteStandardUser(HttpServletRequest request) {
        String password = request.getHeader("password");
        if (password != null) {
            User loggedUser = getLoggedUser();
            if (passwordEncoder.matches(password, loggedUser.getPassword())) {
                orderRepository.findAllByUser_Id(loggedUser.getId()).forEach(order -> {
                   orderRepository.deleteById(order.getId());
                    });
                userRepository.deleteById(loggedUser.getId());
            } else {
                throw new RuntimeException("Wrong password.");
            }
        } else {
            throw new RuntimeException("Empty password.");
        }
    }
    @Transactional
    public void deleteSocialMediaUserOrdersProducts() {
        orderRepository.findAllByUser_Id(getLoggedUser().getId()).forEach(order -> {
            orderProductRepository.findOrderProductsByOrderId(order.getId()).forEach(orderProduct -> {
                activationCodeRepository.deleteAllByOrderProductId(orderProduct.getId());
                orderProductRepository.delete(orderProduct);
            });
        });
    }
    @Transactional
    public void deleteSocialMediaUser() {
        orderRepository.findAllByUser_Id(getLoggedUser().getId()).forEach(order -> {
            orderRepository.deleteById(order.getId());
            });
        userRepository.deleteById(getLoggedUser().getId());
    }


    UserShippingInfoDto getLoggedUserAccountInfo() {
        return UserAccountInfoDtoMapper.map(getLoggedUser());

    }
}
