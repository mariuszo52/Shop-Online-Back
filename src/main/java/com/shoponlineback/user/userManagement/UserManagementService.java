package com.shoponlineback.user.userManagement;

import com.shoponlineback.user.User;
import com.shoponlineback.user.UserDto;
import com.shoponlineback.user.mapper.UserDtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

@Service
public class UserManagementService {

    private final UserManagementRepository userManagementRepository;

    public UserManagementService(UserManagementRepository userManagementRepository) {
        this.userManagementRepository = userManagementRepository;
    }

    List<UserDto> getAllUsers(){
        Spliterator<User> usersSpliterator = userManagementRepository.findAll().spliterator();
        return StreamSupport.stream(usersSpliterator, false)
                .map(UserDtoMapper::map)
                .toList();
    }
}
