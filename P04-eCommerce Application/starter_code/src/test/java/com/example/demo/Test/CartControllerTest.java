package com.example.demo.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User1;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.persistence.User1;

import com.example.demo.model.requests.ModifyCartRequest;

public class CartControllerTest {
    private CartController cartController;
    private UserController userController;
    private UserRepository userRepo=mock(UserRepository.class);
    private CartRepository cartRepo= mock(CartRepository.class);
    private ItemRepository itemRepo= mock(ItemRepository.class);
    
    @Before
    public void setUp() {
    //	userController= new UserController();
    //	TestUtils.injectObjects(userController, "userRepository",userRepo);
    //	TestUtils.injectObjects(userController, "cartRepository",cartRepo);
    
    	cartController= new CartController();
    //	userController= new UserController();
    	TestUtils.injectObjects(cartController, "userRepository",userRepo);
    	TestUtils.injectObjects(cartController, "cartRepository",cartRepo);
    	TestUtils.injectObjects(cartController, "itemRepository",itemRepo);

    }
    @Test
    public void addToTheCart() throws Exception{

    	User1 user=new User1();
        user.setId(1L);
        user.setUsername("username");
    	Item item=new Item();
        item.setId(1L);
        item.setName("productName");
        item.setDescription("Description for productName");
        item.setPrice(new BigDecimal(5.55));
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);

        
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(java.util.Optional.ofNullable(item));

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(item.getId());
        r.setQuantity(3);
        r.setUsername(user.getUsername());
        
        System.out.println("getStatusCodeValue="+r.getUsername()); 

    	final ResponseEntity<Cart> response =	cartController.addTocart(r);
    	assertNotNull(response );
    	assertEquals(200,response .getStatusCodeValue());
        Cart cc=	response .getBody();
   // System.out.println("getStatusCode"+cc.getUser().getUsername()); 
    
    assertNotNull(cc);
  //  assertEquals("Username", cc.getUser().getUsername());
    assertEquals(user,cc.getUser());
    assertEquals(item,cc.getItems().get(0));

    }
    @Test
    public void RemoveFromCart() {
    	User1 user=new User1();
        user.setId(1L);
        user.setUsername("username");
    	Item item=new Item();
        item.setId(1L);
        item.setName("productName");
        item.setDescription("Description for productName");
        item.setPrice(new BigDecimal(5.55));
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        user.setCart(cart);
    	 when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
         when(itemRepo.findById(item.getId())).thenReturn(java.util.Optional.ofNullable(item));
         
         user.getCart().addItem(item);
         System.out.println("getuser="+user.getCart().getItems().size()); 

         assertEquals(1,user.getCart().getItems().size());
         ModifyCartRequest r = new ModifyCartRequest();
         r.setItemId(item.getId());
         r.setQuantity(1);
         r.setUsername(user.getUsername());
         final ResponseEntity<Cart> response = cartController.removeFromcart(r);

         assertNotNull(response);
         assertEquals(200,response.getStatusCodeValue());

         Cart cr = response.getBody();
         assertNotNull(cr);
         assertEquals(user,cr.getUser());
         cr.addItem(item);
         System.out.println("getitems="+user.getCart().getItems().size()); 
         cr.setTotal(new BigDecimal(1.1));
         assertEquals(1,cr.getItems().size());
         assertEquals(item,cr.getItems().get(0));
         assertEquals(item.getId(),cr.getItems().get(0).getId());
         assertEquals(item.getName(),cr.getItems().get(0).getName());
         assertEquals(item.getDescription(),cr.getItems().get(0).getDescription());
         assertEquals(item.getPrice(),cr.getItems().get(0).getPrice());

    }
  
    
  

    @Test
    public void addToCartNoItemPresent(){

        when(userRepo.findByUsername("Username")).thenReturn(new User1());
        when(itemRepo.findById(1L)).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest =new ModifyCartRequest();
        modifyCartRequest.setUsername("Username");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        verify(itemRepo, times(1)).findById(1L);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void RemoveFromCartNoItemPresent(){

        when(userRepo.findByUsername("Username")).thenReturn(new User1());
        when(itemRepo.findById(1L)).thenReturn(Optional.empty());

        ModifyCartRequest modifyCartRequest =new ModifyCartRequest();
        modifyCartRequest.setUsername("Username");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        verify(itemRepo, times(1)).findById(1L);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void addToCartNoUserFOUND(){

        ModifyCartRequest modifyCartRequest =new ModifyCartRequest();
        
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void RemoveFromCartNoUserFOUND(){

        ModifyCartRequest modifyCartRequest =new ModifyCartRequest();
        
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

}
