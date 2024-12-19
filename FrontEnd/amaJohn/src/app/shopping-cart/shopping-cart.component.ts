import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { Product } from '../models/product.model';
import { Cart } from '../models/cart.model';
import { CartItems } from '../models/cartItems.model';
import { CartItem } from '../models/cartitem.model';
import { SFLitems } from '../models/sfls.model';
import { CartServiceService } from '../services/cart-service.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {
  userCart: any;
  cart: any;
  cartlength: any;
  sfllength: any;
  productsListCart: Product[] = [];
  productNotAvailableCart: Product[] = [];
  productsListSFL: Product[] = [];
  productMap: any;
  sflMap: any;
  subtotal: number = 0;
  tax: number = 0;
  shipping: number = 10;
  total:number = 0;
  

  constructor(private readonly auth: AuthService, private proService: ProductServiceService, private router: Router, private cartService: CartServiceService){}

  ngOnInit(): void {
    try {
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }

      this.getCart(token);
    } catch (error: any) {
      throw new Error(error)
    }
  }

  async getCart(token: string) {
    try {
      // Fetch cart and cart items
      const cart: Cart = (await this.cartService.getUserCart(token)).cart;
      this.userCart = cart;
  
      const cartItemResponse: CartItems = (await this.cartService.getUserCartItems(token)).cartItems;
      this.productMap = cartItemResponse.items; // This get all user's productId in cart

      const sflItemResponse: SFLitems = (await this.cartService.getUserSFLItems(token)).sflItems;
      this.sflMap = sflItemResponse.items; // This get all user's productId in save for later cart
  
      // Calculate length and initialize subtotal
      this.cartlength = Object.keys(this.productMap).length;
      this.sfllength = Object.keys(this.sflMap).length;
      this.subtotal = 0;
  
      // Iterate through each product in the productMap using for...in loop
      for (const productId in this.productMap) {
        if (this.productMap.hasOwnProperty(productId)) {
          const cartItem = this.productMap[productId]; // This will be a item in the cart
  
          // Fetch product details
          this.proService.getProductById(productId).subscribe(data => {
            // Add item that are available
            if (data.quantity > 0 && data.quantity >= cartItem.quantity) {
              this.productsListCart.push(data);
              // Using the quantity from cartItem and the product data to calculate subtotal
              this.calSubTotal(data.price.amount, cartItem.quantity);
              this.calTax(this.subtotal, 7.25);
              this.calTotal();
            } else {
              // Add items that are not available in here
              this.productNotAvailableCart.push(data);
            }
          });
        }
      }

      // Iterate through each product in the productMap using for...in loop
      for (const productId in this.sflMap) {
        if (this.sflMap.hasOwnProperty(productId)) {
          const cartItem = this.sflMap[productId]; // This will be a Save For Later Cart
  
          // Fetch product details
          this.proService.getProductById(productId).subscribe(data => {
            this.productsListSFL.push(data);
          });
        }
      }
    } catch (error) {
      console.error('Error fetching cart:', error);
    }
  }

  selectItem(productId: string){
    this.router.navigate([`/products/${productId}`]);
  }

  formatPrice(price: number): string {
    return price.toFixed(2);
  }

  // Use to add or subtract item from cart
  updateCart(productId: string, quanity: number){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.cartService.addToUserCart(token, productId, quanity).then(() => {
        window.location.reload();
      })
      
    } 
    catch (error: any) {
      throw new Error(error)
    }
  }

  // Move item from cart to sfl cart
  saveForLater(productId: string, quantity: number){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.cartService.addToUserSFL(token, productId, quantity).then(() => {
        this.deleteItem(productId);
      })
      
    } 
    catch (error: any) {
      throw new Error(error)
    }
  }

  // Move item from sfl cart to cart
  moveToCart(productId: string, quantity: number){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.cartService.addToUserCart(token, productId, quantity).then(() => {
        this.deleteFromSaveForLater(productId);
      })
      
    } 
    catch (error: any) {
      throw new Error(error)
    }
  }

  deleteItem(productId: string){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.cartService.updateUserCart(token, productId).then(() => {
        window.location.reload();
      })
      
    } 
    catch (error: any) {
      throw new Error(error)
    }
  }

  deleteFromSaveForLater(productId: string){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.cartService.updateUserSFL(token, productId).then(() => {
        window.location.reload();
      })
      
    } 
    catch (error: any) {
      throw new Error(error)
    }
  }

  checkout() {
    const token = localStorage.getItem('token');
    if (!token) {
      window.alert('No token found. Please log in again.');
      return;
    }
  
    this.cartService
      .checkout(token, this.productsListCart, this.productMap)
      .then(() => {
        //window.location.reload(); // Successful checkout
      })
      .catch((error: any) => {
        // Handle the "out of stock" error
        window.alert(`Checkout failed: ${error.message}`);
        window.location.reload();
        
      });
  }

  calSubTotal(price: number, quantity: number) {
    const productTotal = price * quantity;
    this.subtotal = (Math.round((this.subtotal + productTotal) * 100) / 100);

  }
  
  calTax(subtotal: number, taxRate: number) {
    this.tax = (Math.round(subtotal * taxRate) / 100);
  }
  
  calTotal() {
    this.total = (Math.round((this.tax + this.subtotal + this.shipping) * 100) / 100);
  }

  goBack() {
    window.history.back();
  }



}
