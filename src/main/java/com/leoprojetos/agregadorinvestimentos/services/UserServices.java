package com.leoprojetos.agregadorinvestimentos.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leoprojetos.agregadorinvestimentos.controllers.DTO.CreateUserDTO;
import com.leoprojetos.agregadorinvestimentos.controllers.DTO.UpdateUserDTO;
import com.leoprojetos.agregadorinvestimentos.entity.User;
import com.leoprojetos.agregadorinvestimentos.repository.UserRepository;

@Service
public class UserServices {

	@Autowired
	private UserRepository userRepository;

	public UUID createUser(CreateUserDTO createUserDTO) {

		// ✅ NÃO precisa passar creationTimestamp, o JPA preenche automático
		var entity = new User(
				null,
				createUserDTO.username(),
				createUserDTO.email(),
				createUserDTO.password(),
				Instant.now(),    // updateTimestamp
				null              // creationTimestamp (vai ser preenchido pelo @CreationTimestamp)
		);

		var userSaved = userRepository.save(entity);

		return userSaved.getUserId();
	}
	
	public Optional<User> getUserByID(String userId) {
		return userRepository.findById(UUID.fromString(userId));
	}
	
	public List<User> listUsers() {
		return userRepository.findAll();
	}
	
	public void deleteById(String userId) {
		var id = UUID.fromString(userId);
		var userExists = userRepository.existsById(id);
		
		if (userExists) {
			userRepository.deleteById(id);
		} 
	}
	
	public void updateUser(String userId, UpdateUserDTO updateUserDTO) {
		var id = UUID.fromString(userId);
		var userEntity = userRepository.findById(id);
		
		if (userEntity.isPresent()){
			var user = userEntity.get();
			
			if (updateUserDTO.username() != null) {
				user.setUsername(updateUserDTO.username());
			}
			if (updateUserDTO.password() != null) {
				user.setPassword(updateUserDTO.password());
			}
			
			userRepository.save(user);
		}
	}
}
