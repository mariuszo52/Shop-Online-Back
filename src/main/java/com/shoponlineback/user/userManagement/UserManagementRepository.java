package com.shoponlineback.user.userManagement;

import com.shoponlineback.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserManagementRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
