package com.shoponlineback.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String username);
}