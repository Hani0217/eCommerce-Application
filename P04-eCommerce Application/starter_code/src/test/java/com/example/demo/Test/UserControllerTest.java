package com.example.demo.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User1;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo=mock(UserRepository.class);
    private CartRepository cartRepo= mock(CartRepository.class);
    private BCryptPasswordEncoder encoder=mock( BCryptPasswordEncoder.class);
    @Before
    public void setUp() {
    	userController= new UserController();
    	TestUtils.injectObjects(userController, "userRepository",userRepo);
    	TestUtils.injectObjects(userController, "cartRepository",cartRepo);
    	TestUtils.injectObjects(userController, "bCryptpasswordEncoder",encoder);
    	
    }
    @Test
    public void create_user_happy_path() throws Exception{
    	when(encoder.encode( "testPassword")).thenReturn("thisIsHashed");
    	CreateUserRequest r=new CreateUserRequest();
    	
    	r.setUsername("test");
    	r.setPassword("testPassword");
    	r.setConfirmpassword("testPassword");
    	final ResponseEntity <User1> response= userController.createUser(r);
    	assertNotNull(response);
    	assertEquals(200,response.getStatusCodeValue());
    	
    	User1 u=response.getBody();
        System.out.print("user u="+u.getUsername()); 

    	assertNotNull(u);
     //   System.out.print("/////user u af="+u.getUsername()); 

    	assertEquals( 0,u.getId());
    	assertEquals( "test",u.getUsername());
    	assertEquals( "thisIsHashed",u.getPassword());

    }
    
    @Test
    public void findByIdFoundTest(){

    	 User1 user =new User1();
    	 user.setId(1L);
         when(userRepo.findById(1L)).thenReturn(Optional.of(user));
         ResponseEntity<User1> response = userController.findById(1L);

         assertNotNull(response);
         assertEquals(200, response.getStatusCodeValue());
       //  System.out.println("Ig="+response.getBody().getId()); 

         assertEquals(user.getId(), response.getBody().getId());
         assertEquals(1L, response.getBody().getId());

    }
    
    @Test
    public void findByUsernameFoundTest(){

    	User1 user =new User1();
    	user.setUsername("UsernameNb");
        when(userRepo.findByUsername("Username")).thenReturn(user);
        ResponseEntity<User1> response = userController.findByUserName("Username");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        
    	User1 u=response.getBody();
    	assertNotNull(u);
    	
        assertEquals(u.getUsername(), response.getBody().getUsername());
        System.out.print("GfG1="+u.getUsername()); 
        System.out.print("GfG1="+response.getBody().getUsername()); 
        assertEquals("UsernameNb", u.getUsername());

    }
    @Test
    public void findByUsernameNotFoundTest(){

        ResponseEntity<User1> response = userController.findByUserName("Username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void checkPasswordLength() throws Exception {
    	CreateUserRequest r=new CreateUserRequest();
        r.setPassword("word");
        r.setConfirmpassword("word");
    	when(encoder.encode( "testPass")).thenReturn("word");
    	 ResponseEntity<User1> response = userController.createUser(r);
         assertNotNull(response);
         System.out.println("getStatusCodeValue="+response.getStatusCodeValue()); 
         assertEquals(400, response.getStatusCodeValue());
    }
    	
}
