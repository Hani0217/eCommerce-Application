package com.example.demo.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//import org.hibernate.annotations.common.util.impl.Log_.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User1;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import jdk.internal.net.http.common.Log;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptpasswordEncoder;
	
   private static final Logger logger =  (Logger) LoggerFactory.getLogger(UserController.class);

	
	@GetMapping("/id/{id}")
	public ResponseEntity<User1> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User1> findByUserName(@PathVariable String username) {
		User1 user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User1> createUser(@RequestBody CreateUserRequest createUserRequest) {
		
		try {
		User1 user = new User1();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
			!createUserRequest.getPassword().equals(createUserRequest.getConfirmpassword())){
			//Log.logError("Error with user Password {}",createUserRequest.getUsername());
			System.err.println("Error with user password. Cannot create user = " + createUserRequest.getUsername());

			return ResponseEntity.badRequest().build();
		}
	
		user.setPassword(bCryptpasswordEncoder.encode(createUserRequest.getPassword()));
		
		userRepository.save(user);
		logger.info("The User : "+ createUserRequest.getUsername()+"  Created successes");
		return ResponseEntity.ok(user);
		

		}catch (Exception e) {
			 logger.error("Error create user: " + createUserRequest.getUsername() );
			
			// return new ResponseEntity<>(new ResponseResource("Error creating user!"), HttpStatus.INTERNAL_SERVER_ERROR);
			 return new ResponseEntity(("Error creating user!"), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
	}
	
}
