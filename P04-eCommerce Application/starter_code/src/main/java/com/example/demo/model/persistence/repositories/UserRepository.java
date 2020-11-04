package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.User1;

public interface UserRepository extends JpaRepository<User1, Long> {
	User1 findByUsername(String username);
}
