import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { Product } from '../models/product.model';
import { Cart } from '../models/cart.model';

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

  async getCart(token: string){
    const cart: Cart = (await this.auth.getUserCart(token)).cart;
    this.userCart = cart;
    this.productMap = cart.cartOrder;
    this.length = Object.keys(this.productMap).length;
    Object.entries(this.productMap).forEach(([productId, quantity]) => {
      this.proService.getProductById(productId).subscribe(data => {
        // Push the actual product data into productsList
        this.productsList.push(data);
      });
    });

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

}
