import { Component } from '@angular/core';
import { ProductServiceService } from '../services/product-service.service';
import { Router } from '@angular/router';
import { CartServiceService } from '../services/cart-service.service';
import { OrderHistory } from '../models/orderHistory.model';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.css'
})
export class OrderHistoryComponent {
  orderHistory: any;
  orders: Map<string, Product[]> = new Map();
  
  constructor( private proService: ProductServiceService, private router: Router, private cartService: CartServiceService){}

  ngOnInit(): void {
    try {
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }

      this.getOrders(token);
    } catch (error: any) {
      throw new Error(error)
    }
  }

  async getOrders(token: string) {
    try {
      const orderHistory: any = (await this.cartService.getOrderHistory(token));
      this.orderHistory = orderHistory.orderHistories;
      for (let orders of this.orderHistory){
        console.log(orders)
        this.orders.set(orders.id, [])
        for (let productId of this.getProductsKeys(orders.products)){
          this.proService.getProductById(productId).subscribe(data => {
            this.orders.get(orders.id)?.push(data)
          })
        }
      }
      console.log(this.orders)

    } catch (error) {
      console.error('Error fetching order history:', error);
    }

  }

  getProductsKeys(products: { [key: string]: number }): string[] {
    return Object.keys(products); 
  }

  async getProduct(id: string){
    try {
      this.proService.getProductById(id).subscribe(data => {
        return data;
      })
      
    } catch (error){
      console.log('Error fetching product details', error);
    }
  }
  
  selectItem(productId: string){
    this.router.navigate([`/products/${productId}`]);
  }

}
