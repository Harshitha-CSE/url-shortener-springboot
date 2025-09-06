package com.url.urlshortenersb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.url.urlshortenersb.models.User;
import java.util.Optional;

@Repository
public interface  UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);

}
