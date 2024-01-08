package com.shoponlineback.user.userManagement;

import com.shoponlineback.exceptions.user.UserNotFoundException;
import com.shoponlineback.exceptions.userRole.UserRoleNotFoundException;
import com.shoponlineback.user.User;
import com.shoponlineback.user.UserDto;
import com.shoponlineback.user.dto.UserEmailUpdateDto;
import com.shoponlineback.user.dto.UsernameUpdateDto;
import com.shoponlineback.user.mapper.UserDtoMapper;
import com.shoponlineback.userRole.UserRole;
import com.shoponlineback.userRole.UserRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

@Service
public class UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final UserRoleRepository userRoleRepository;

    public UserManagementService(UserManagementRepository userManagementRepository, UserRoleRepository userRoleRepository) {
        this.userManagementRepository = userManagementRepository;
        this.userRoleRepository = userRoleRepository;
    }

    Page<UserDto> getAllUsers(int page) {
        int size = 50;
        Spliterator<User> usersSpliterator = userManagementRepository.findAll().spliterator();
        List<UserDto> list = StreamSupport.stream(usersSpliterator, false)
                .map(UserDtoMapper::map).toList();
        List<UserDto> currentPage = list.subList(Math.min((page * size), list.size()),
                Math.min((page * size + size), list.size()));
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(currentPage, pageRequest, list.size());
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
        if (!userManagementRepository.existsByEmail(emailUpdateDto.getEmail())) {
            User user = userManagementRepository.findById(emailUpdateDto.getUserId())
                    .orElseThrow(UserNotFoundException::new);
            user.setEmail(emailUpdateDto.getEmail());
        } else {
            throw new RuntimeException("Email is already taken.");
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
        user.setUserRole(userRole);
    }
    @Transactional
    public void deleteUser(Long userId){
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
