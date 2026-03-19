package com.leoprojetos.agregadorinvestimentos.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leoprojetos.agregadorinvestimentos.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{
}
