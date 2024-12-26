package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUserName(String name);
    Optional<User> findByAccount(String account);
}
