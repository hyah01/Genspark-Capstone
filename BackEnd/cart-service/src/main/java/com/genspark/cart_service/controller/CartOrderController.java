package com.genspark.cart_service.controller;

import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.services.CartOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartorder")
public class CartOrderController {
    @Autowired
    private CartOrderService service;

    @PostMapping // Add a single order to the databased
    public CartOrder add(@RequestBody CartOrder cartOrder){
        return service.addCartOrder(cartOrder);
    }

    @PutMapping // Update a single order in the database, can only update the quantity
    public CartOrder update(@RequestBody CartOrder cartOrder){
        CartOrder updated = service.getById(cartOrder.getId());
        updated.setQuantity(cartOrder.getQuantity());
        return service.updateCartOrder(updated);
    }

    @DeleteMapping("/byId/{id}") // delete based on the cartOrderId
    public String delete(@PathVariable String id){
        return service.deleteCartOrder(id);
    }

    @GetMapping // get all the cartOrders
    public List<CartOrder> findAll(){
        return service.getAllCartOrder();
    }

    @GetMapping("/{id}") // get all the cartOrder based on CartID of user ( Get all items in user's cart )
    public List<CartOrder> getByCartId(String id){
        return service.getAllCartOrderByCartId(id);
    }

    @GetMapping("/byId/{id}") // Get cartOrder by ID
    public CartOrder getById(String id){
        return service.getById(id);
    }

    @DeleteMapping("/{cartId}") // Delete all the CartOrder when user delete account
    public String deleteAllByCartId(@PathVariable String cartId) {
        return service.deleteAllByCartId(cartId);
    }

}
