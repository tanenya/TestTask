package com.commerzbank.task.repository;

import com.commerzbank.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Test task Commerzbank
 *
 * Repository for {@link User}
 *
 * @author vtanenya
 * */

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
