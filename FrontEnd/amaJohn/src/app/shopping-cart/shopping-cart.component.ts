import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {
  userCart: any;
  cart: any;
  rawProduct: any;
  productsList: Product[] = [];

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
    this.userCart = (await this.auth.getUserCart(token)).cart;
    console.log(this.userCart.cartOrder);
    this.rawProduct = Object.keys(this.userCart.cartOrder);
    for (const key of this.rawProduct){
      this.proService.getProductById(key).subscribe(data => {
        // Push the actual product data into productsList
        this.productsList.push(data);
        console.log(data);
      });
    }
  }

}
