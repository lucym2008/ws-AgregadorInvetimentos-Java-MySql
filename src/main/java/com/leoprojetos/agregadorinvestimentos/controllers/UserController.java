package com.leoprojetos.agregadorinvestimentos.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leoprojetos.agregadorinvestimentos.controllers.DTO.CreateUserDTO;
import com.leoprojetos.agregadorinvestimentos.controllers.DTO.UpdateUserDTO;
import com.leoprojetos.agregadorinvestimentos.entity.User;
import com.leoprojetos.agregadorinvestimentos.services.UserServices;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	@Autowired
	private UserServices userServices;
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
		var id = userServices.createUser(createUserDTO);
		return ResponseEntity.created(URI.create("/v1/users/" + id.toString())).build();
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
		var user = userServices.getUserByID(userId);
		if (user.isPresent()) {			
			return ResponseEntity.ok(user.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getUsers() {
		var users =  userServices.listUsers();
		return ResponseEntity.ok(users);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId) {
		userServices.deleteById(userId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, 
			                               @RequestBody UpdateUserDTO updateUserDTO) {
		userServices.updateUser(userId, updateUserDTO);
		return ResponseEntity.noContent().build();
	}
	

}
