package com.shoponlineback.user.userManagement;

import com.shoponlineback.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserManagementRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
