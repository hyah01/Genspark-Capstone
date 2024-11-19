//package com.genspark.cart_service.controller;
//
//import com.genspark.cart_service.model.Cart;
//import com.genspark.cart_service.model.CartOrder;
//import com.genspark.cart_service.services.CartOrderService;
//import com.genspark.cart_service.services.CartService;
//import com.genspark.cart_service.util.CartJwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/cartorder")
//public class CartOrderController {
//    @Autowired
//    private CartOrderService service;
//
//    @Autowired
//    private CartJwtUtil jwtUtil;
//
//    @Autowired
//    private CartService cartService;
//
////    @PostMapping // Add a single order to the databased
////    public CartOrder add(@RequestBody CartOrder cartOrder){
////        return service.addCartOrder(cartOrder);
////    }
//
//
//    @PostMapping // Add a single order to the databased
//    public ResponseEntity<CartOrder> add(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartOrder cartOrder){
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        try {
//            // Validate the token
//            String username = jwtUtil.extractUsername(token);
//            System.out.println(username);
//            if (username == null || jwtUtil.tokenValidate(token)) {
//                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
//            }
//
//            // Fetch the cart
//            Cart cart = cartService.getCartByEmail(username);
//            cartOrder.setCartId(cart.getId());
//            CartOrder newCartOrder = service.addCartOrder(cartOrder);
//            cart.getCartOrder().add(newCartOrder);
//            cartService.updateCart(cart);
//
//            return ResponseEntity.ok(newCartOrder);
//        } catch (Exception e) {
//            e.printStackTrace(); // Print stack trace for debugging
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
//
//    @PutMapping // Update a single order in the database, can only update the quantity
//    public CartOrder update(@RequestBody CartOrder cartOrder){
//        CartOrder updated = service.getCartOrderById(cartOrder.getId());
//        updated.setQuantity(cartOrder.getQuantity());
//        return service.updateCartOrder(updated);
//    }
//
//    @DeleteMapping("/byId/{id}") // delete based on the cartOrderId
//    public ResponseEntity<String> delete(@PathVariable String id){
//        String string = service.deleteCartOrder(id);
//        if (string == null) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(string);
//        }
//    }
//
//    @GetMapping // get all the cartOrders
//    public List<CartOrder> findAll(){
//        return service.getAllCartOrder();
//    }
//
//    @GetMapping("/{id}") // get all the cartOrder based on CartID of user ( Get all items in user's cart )
//    public List<CartOrder> getByCartId(String id){
//        return service.getAllCartOrderByCartId(id);
//    }
//
//    @GetMapping("/byId/{id}") // Get cartOrder by ID
//    public ResponseEntity<CartOrder> getCartOrderById(@PathVariable String id){
//        CartOrder cartOrder = service.getCartOrderById(id);
//        if (cartOrder == null) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(cartOrder);
//        }
//    }
//
//    @DeleteMapping("/{cartId}") // Delete all the CartOrder when user delete account
//    public ResponseEntity<String> deleteAllByCartId(@PathVariable String cartId) {
//        String string = service.deleteAllByCartId(cartId);
//        if (string == null) {
//            return ResponseEntity.notFound().build();
//        } else {
//            return ResponseEntity.ok(string);
//        }
//    }
//
//    // Exception handling for this controller
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
//    }
//
//}
