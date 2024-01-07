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

    List<UserDto> getAllUsers() {
        Spliterator<User> usersSpliterator = userManagementRepository.findAll().spliterator();
        return StreamSupport.stream(usersSpliterator, false)
                .map(UserDtoMapper::map)
                .toList();
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
}
