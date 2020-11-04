package com.example.demo.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User1;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepo = mock(UserRepository.class);

    private final OrderRepository orderRepo = mock(OrderRepository.class);

    
    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);

    }
    
    @Test
    public void submitTest(){
    	
    	 User1 user =new User1();
    	 user.setUsername("Username0");
         user.setId(1L);
         Item item=new Item();
         item.setId(1L);
         item.setName("ProductName");
         item.setDescription("DescriptionItem");
         item.setPrice(new BigDecimal(5.55));
        Cart cart = new Cart();
         cart.setId(1L);
         cart.setUser(user);
         cart.addItem(item);

         user.setCart(cart);
    	 when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

         final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

         assertNotNull(response);
         assertEquals(200,response.getStatusCodeValue());

         UserOrder userOrder = response.getBody();
         assertNotNull(userOrder);
         assertEquals(cart.getItems().size(),userOrder.getItems().size());
         assertEquals(cart.getItems().get(0),userOrder.getItems().get(0));
         assertEquals(cart.getItems().get(0).getId(),userOrder.getItems().get(0).getId());
         assertEquals(cart.getItems().get(0).getName(),userOrder.getItems().get(0).getName());
         assertEquals(cart.getItems().get(0).getDescription(),userOrder.getItems().get(0).getDescription());
         assertEquals(cart.getItems().get(0).getPrice(),userOrder.getItems().get(0).getPrice());
    }
    @Test
	public void getOrdersForUser() throws Exception {
    	 User1 user =new User1();
    	 user.setUsername("Username");
    	when(userRepo.findByUsername("Username")).thenReturn(user);

    	ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("Username");

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        List<UserOrder> userOrders = responseEntity.getBody();
	}
    @Test
    public void ordersByUserNullUser(){

        User1 user = new User1();
        Cart cart = user.getCart();
        List<Item> itemList = new ArrayList<>();
        when(userRepo.findByUsername("Username")).thenReturn(null);
        orderController.submit("Username");
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("Username");
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }


}
