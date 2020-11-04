package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User1;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	
	@Autowired
	private UserRepository userRepository;
//	userRepository.
	@Autowired
	private OrderRepository orderRepository;
	
	   private static final Logger logger =  (Logger) LoggerFactory.getLogger(UserController.class);

	//@PostMapping("/submit/{username}")
//	@PostMapping("/submit/{username}"+userRepository.ge)
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		try {
		User1 user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("User :"+username+" not found " );

			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		logger.info("Order for user :"+username+" submitted." );

		return ResponseEntity.ok(order);
		}catch (Exception e) {
			 logger.error("Error Submit  ! ");
		    	return new ResponseEntity(("Error Submit For User!"), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		try {
		User1 user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("User :"+username+" not found " );
			return ResponseEntity.notFound().build();
		}
		logger.info("Order history for user {} successfully.", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
		}catch (Exception e) {
			 logger.error("Error get OrderForUser  ! ");
			return new ResponseEntity(("Error get Order For User!"), HttpStatus.INTERNAL_SERVER_ERROR);			

		}
	}
}
