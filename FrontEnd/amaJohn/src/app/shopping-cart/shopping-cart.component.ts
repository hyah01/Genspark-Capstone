import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { Product } from '../models/product.model';
import { Cart } from '../models/cart.model';
import { CartItems } from '../models/cartItems.model';
import { CartItem } from '../models/cartitem.model';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {
  userCart: any;
  cart: any;
  length: any;
  productsList: Product[] = [];
  productMap: any;
  subtotal: number = 0;
  tax: number = 0;
  shipping: number = 10;
  total:number = 0;
  

  constructor(private readonly auth: AuthService, private proService: ProductServiceService, private router: Router){}

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
      const cart: Cart = (await this.auth.getUserCart(token)).cart;
      this.userCart = cart;
  
      const cartItemResponse: CartItems = (await this.auth.getUserCartItems(token)).cartItems;
      this.productMap = cartItemResponse.items; // This is a map of productId -> CartItem
  
      // Calculate length and initialize subtotal
      this.length = Object.keys(this.productMap).length;
      this.subtotal = 0;
  
      // Iterate through each product in the productMap using for...in loop
      for (const productId in this.productMap) {
        if (this.productMap.hasOwnProperty(productId)) {
          const cartItem = this.productMap[productId]; // This will be a CartItem
  
          // Fetch product details
          this.proService.getProductById(productId).subscribe(data => {
            this.productsList.push(data);
            // Using the quantity from cartItem and the product data to calculate subtotal
            this.calSubTotal(data.price.amount, cartItem.quantity);
            this.calTax(this.subtotal, 7.25);
            this.calTotal();
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

  updateCart(productId: string, quanity: number){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.auth.addToUserCart(token, productId, quanity).then(() => {
        window.location.reload();
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
      console.log("test1")
      this.auth.updateUserCart(token, productId).then(() => {
        window.location.reload();
      })
      
    } 
    catch (error: any) {
      throw new Error(error)
    }
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
