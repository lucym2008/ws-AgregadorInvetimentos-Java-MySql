package com.leoprojetos.agregadorinvestimentos.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.leoprojetos.agregadorinvestimentos.controller.dto.CreateAccountDto;
import com.leoprojetos.agregadorinvestimentos.entity.Account;
import com.leoprojetos.agregadorinvestimentos.entity.BillingAddress;
import com.leoprojetos.agregadorinvestimentos.repository.AccountRepository;
import com.leoprojetos.agregadorinvestimentos.repository.BillingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.leoprojetos.agregadorinvestimentos.controller.dto.CreateUserDto;
import com.leoprojetos.agregadorinvestimentos.controller.dto.UpdateUserDto;
import com.leoprojetos.agregadorinvestimentos.entity.User;
import com.leoprojetos.agregadorinvestimentos.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static java.util.Objects.isNull;

@Service
public class UserService {

	private UserRepository userRepository;
	private AccountRepository accountRepositoy;

	private BillingAddressRepository billingAddressRepository;

	public UserService(UserRepository userRepository,
					   AccountRepository accountRepositoy,
					   BillingAddressRepository billingAddressRepository) {
		this.userRepository = userRepository;
		this.accountRepositoy = accountRepositoy;
		this.billingAddressRepository = billingAddressRepository;
	}

	public UUID createUser(CreateUserDto createUserDto) {

		// DTO -> ENTITY
		var entity = new User(
				null,				createUserDto.username(),
				createUserDto.email(),
				createUserDto.password(),
				Instant.now(),
				null);

		var userSaved = userRepository.save(entity);

		return userSaved.getUserId();
	}

	public Optional<User> getUserById(String userId) {

		return userRepository.findById(UUID.fromString(userId));
	}

	public List<User> listUsers() {
		return userRepository.findAll();
	}

	public void updateUserById(String userId,
							   UpdateUserDto updateUserDto) {

		var id = UUID.fromString(userId);

		var userEntity = userRepository.findById(id);

		if (userEntity.isPresent()) {
			var user = userEntity.get();

			if (updateUserDto.username() != null) {
				user.setUsername(updateUserDto.username());
			}

			if (updateUserDto.password() != null) {
				user.setPassword(updateUserDto.password());
			}

			userRepository.save(user);
		}

	}

	public void deleteById(String userId) {

		var id = UUID.fromString(userId);

		var userExists = userRepository.existsById(id);

		if (userExists) {
			userRepository.deleteById(id);
		}
	}

	public void createAccount(String userId, CreateAccountDto createAccountDto) {

		var user = userRepository.findById(UUID.fromString(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não existe"));

		if (isNull(user.getAccounts())) {
			user.setAccounts(new ArrayList<>());
		}

		// DTO -> ENTITY
		var account = new Account(
				null,				user,
				null,
				createAccountDto.description(),
				new ArrayList<>()
		);

		var accountCreated = accountRepositoy.save(account);

		var billingAddress = new BillingAddress(
				null,
				accountCreated,
				createAccountDto.street(),
				createAccountDto.number()
		);

		billingAddressRepository.save(billingAddress);
	}

	@Transactional(readOnly = true)  // ← ADICIONA ISSO
	public List<Account> findAccounts(String userId) {
		var user = userRepository.findById(UUID.fromString(userId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não existe"));

		return user.getAccounts();
	}
}