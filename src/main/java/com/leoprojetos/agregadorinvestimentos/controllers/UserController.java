package com.leoprojetos.agregadorinvestimentos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leoprojetos.agregadorinvestimentos.entity.User;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody String body) {
		
		return null;
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable String userId) {
		
		return null;
	}

}
