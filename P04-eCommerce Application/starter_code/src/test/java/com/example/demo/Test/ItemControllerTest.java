package com.example.demo.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.junit.Before;
import org.junit.Test;


import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;


public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }
    
    
    @Test
    public void getItemsTest(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> itemList =response.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(itemList);
    }
    
    @Test
    public void getItemByIdTest(){
        List<Item> items = new ArrayList<>();
        items.add(item().get());
    	when(itemRepo.findById(1L)).thenReturn(item());

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
      //  System.out.println("itemId="+response.getBody().getId()); 

        Item item= response.getBody();
        assertNotNull(item);
    //    assertEquals (item.getId(),11L);


    }
    
    @Test
    public void getItemByNameTest(){

        List<Item> items = new ArrayList<>();
        items.add(item().get());
        when(itemRepo.findByName("Created Item")).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Created Item");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(items, response.getBody());
     //   System.out.println("item="+response.getBody().get(0).getId()); 
        

        assertEquals("NameOfProduct", response.getBody().get(0).getName());
        assertEquals("Description", response.getBody().get(0).getDescription());
        assertEquals(BigDecimal.valueOf(10), response.getBody().get(0).getPrice());
     //   assertEquals(11L, response.getBody().get(0).getId());
    }
    
    @Test
    public void getItemsByNameNotFoundTest(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("name");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    private Optional<Item> item(){
        Optional<Item> item = Optional.of(new Item());
        item.get().setDescription("Description");
        item.get().setName("NameOfProduct");
        item.get().setPrice(BigDecimal.valueOf(10));
        item.get().setId(11L);
        return item;
    }
 
}
