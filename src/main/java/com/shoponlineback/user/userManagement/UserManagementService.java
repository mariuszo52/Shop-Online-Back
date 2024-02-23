package com.shoponlineback.user.userManagement;

import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.order.OrderRepository;
import com.shoponlineback.orderProduct.OrderProductRepository;
import com.shoponlineback.orderProduct.activationCode.ActivationCodeRepository;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserDto;
import com.shoponlineback.user.UserType;
import com.shoponlineback.user.dto.UserEmailUpdateDto;
import com.shoponlineback.user.dto.UsernameUpdateDto;
import com.shoponlineback.user.mapper.UserDtoMapper;
import com.shoponlineback.userRole.UserRole;
import com.shoponlineback.userRole.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import static com.shoponlineback.user.UserService.getLoggedUser;

@Service
public class UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final UserRoleRepository userRoleRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ActivationCodeRepository activationCodeRepository;

    public UserManagementService(UserManagementRepository userManagementRepository, UserRoleRepository userRoleRepository,
                                 OrderRepository orderRepository, OrderProductRepository orderProductRepository,
                                 ActivationCodeRepository activationCodeRepository) {
        this.userManagementRepository = userManagementRepository;
        this.userRoleRepository = userRoleRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.activationCodeRepository = activationCodeRepository;
    }

    Page<UserDto> getAllUsers(int page) {
        PageRequest pageRequest = PageRequest.of(page, 50);
        return userManagementRepository.findAll(pageRequest)
                .map(UserDtoMapper::map);
    }

    @Transactional
    public void updateUsername(UsernameUpdateDto usernameDto) {
        if (!userManagementRepository.existsByUsername(usernameDto.getUsername())) {
            User user = userManagementRepository.findById(usernameDto.getUserId())
                    .orElseThrow(UserNotFoundException::new);
            user.setUsername(usernameDto.getUsername());
        } else {
            throw new RuntimeException("Username is already taken.");
        }
    }

    @Transactional
    public void updateEmail(UserEmailUpdateDto emailUpdateDto) {
        User user = userManagementRepository.findById(emailUpdateDto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        if (userManagementRepository.existsByEmail(emailUpdateDto.getEmail())) {
            throw new RuntimeException("Email is already taken.");
        } else if (user.getType() != UserType.STANDARD) {
            throw new RuntimeException("Unable to change email. This account is not STANDARD type.");
        } else {
            user.setEmail(emailUpdateDto.getEmail());
        }
    }



    @Transactional
    public void updateIsEnabled(UserIsEnabledUpdateDto isEnabledUpdateDto) {
        User user = userManagementRepository.findById(isEnabledUpdateDto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        user.setIsEnabled(isEnabledUpdateDto.getIsEnabled());

    }

    @Transactional
    public void updateUserRole(UserRoleUpdateDto roleUpdateDto) {
        User user = userManagementRepository.findById(roleUpdateDto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        UserRole userRole = userRoleRepository.findUserRoleByName(roleUpdateDto.getRole())
                .orElseThrow(UserRoleNotFoundException::new);
        if (user.getType().name().equals(UserType.STANDARD.name())) {
            user.setUserRole(userRole);
        } else {
            throw new RuntimeException("Unable to change user role. This account is not STANDARD type.");
        }
    }

    @Transactional
    public void deleteUserOrderProducts(Long userId) {
        orderRepository.findAllByUser_Id(userId).forEach(order -> {
            orderProductRepository.findOrderProductsByOrderId(order.getId()).forEach(orderProduct -> {
                activationCodeRepository.deleteAllByOrderProductId(orderProduct.getId());
                orderProductRepository.delete(orderProduct);
            });
        });
    }

    @Transactional
    public void deleteUserAccount(Long userId) {
        orderRepository.findAllByUser_Id(userId).forEach(order -> {
            orderRepository.deleteById(order.getId());
        });
        userManagementRepository.deleteById(userId);
    }


    public UserDto getUserByValues(String searchBy, String value) {
        User user = switch (searchBy) {
            case "id" -> userManagementRepository.findById(Long.parseLong(value))
                    .orElseThrow(UserNotFoundException::new);
            case "username" -> userManagementRepository.getUserByUsername(value)
                    .orElseThrow(UserNotFoundException::new);
            case "email" -> userManagementRepository.getUserByEmail(value)
                    .orElseThrow(UserNotFoundException::new);
            default -> throw new IllegalStateException("Unexpected value: " + searchBy);
        };
        return UserDtoMapper.map(user);
    }
}
