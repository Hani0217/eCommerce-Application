package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User1;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	   private static final Logger logger =  (Logger) LoggerFactory.getLogger(UserController.class);

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		try {
		User1 user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			logger.error("User ="+ user.getUsername()+" not found ");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			logger.error("Item :"+item.get().getName()+" not found ");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);
		logger.info("the User :"+request.getUsername()+" add item :"+item.get().getName()+" to the cart successes");

		return ResponseEntity.ok(cart);
				}catch (Exception e) {		
    	 logger.error("Error add item to the cart ! " );
		return new ResponseEntity(("Error Adding item to the cart user!"), HttpStatus.INTERNAL_SERVER_ERROR);			
	            	}
	}
	
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		try {
		User1 user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			logger.error("User :"+user.getUsername()+" not found ");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			logger.error("Item "+item.get().getName()+" not found " );
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);
		logger.info("the User :"+request.getUsername()+" remove item :"+item.get().getName()+" from the cart successes");

		return ResponseEntity.ok(cart);
		}catch (Exception e) {	
			 logger.error("Error remove item from the cart ! ");
				return new ResponseEntity(("Error Removing item from the cart user!"), HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
		
}
