import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, lastValueFrom, map } from 'rxjs';
import { Product } from '../models/product.model';
import { orderHistoryProducts } from '../models/orderHistoryProducts.model';

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  private BASE_URL = "http://localhost:8060";

  constructor(private http: HttpClient, private route: Router) {}

  async addCart(email: string, token: string): Promise<any>{
    const url = `${this.BASE_URL}/cart`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    const body = {
      email: email,
      cartOrder: {}
    }
    try {
      const response = await lastValueFrom(this.http.post<any>(url, body, {headers} ));
      return  response;
    }catch (error) {
      throw error;
    }
    
  }

  async getUserCart(token: string):Promise<any>{
    const url = `${this.BASE_URL}/cart/get-my-cart`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = await lastValueFrom(this.http.get<any>(url, {headers} ));
      return response;
    } catch (error) {
      throw error;
    }
  }

  async getUserCartItems(token: string):Promise<any>{
    const url = `${this.BASE_URL}/cart-item/get-item`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = await lastValueFrom(this.http.get<any>(url, {headers} ));
      return response;
    } catch (error) {
      throw error;
    }
  }

  async getUserSFLItems(token: string):Promise<any>{
    const url = `${this.BASE_URL}/saveforlater/get-item`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = await lastValueFrom(this.http.get<any>(url, {headers} ));
      return response;
    } catch (error) {
      throw error;
    }
  }

  async addToUserCart(token: string, id: string, quantity: number):Promise<any>{
    const url = `${this.BASE_URL}/cart-item/add-item`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    })
    const body = {
      productId: id,
      quantity: quantity,
    }
    try {
      const response = await lastValueFrom(this.http.put<any>(url,body, {headers} ));
      return response;
    }catch (error) {
      throw error;
    }
  }

  async addToUserSFL(token: string, id: string, quantity: number):Promise<any>{
    const url = `${this.BASE_URL}/saveforlater/add-item`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    })
    const body = {
      productId: id,
      quantity: quantity,
    }
    try {
      const response = await lastValueFrom(this.http.put<any>(url,body, {headers} ));
      return response;
    }catch (error) {
      throw error;
    }
  }

  async updateUserCart(token: string, id: string):Promise<any>{
    const url = `${this.BASE_URL}/cart-item/update-item`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    })
    const body = {
      productId: id,
      quantity: 0,
    }
    try {
      const response = await lastValueFrom(this.http.put<any>(url,body, {headers} ));
      return response;
    }catch (error) {
      throw error;
    }
  }

  async updateUserSFL(token: string, id: string):Promise<any>{
    const url = `${this.BASE_URL}/saveforlater/update-item`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    })
    const body = {
      productId: id,
      quantity: 0,
    }
    try {
      const response = await lastValueFrom(this.http.put<any>(url,body, {headers} ));
      return response;
    }catch (error) {
      throw error;
    }
  }

  async checkout(token: string, products: Product[], productMap: any):Promise<any>{
    const url = `${this.BASE_URL}/cart-item/checkout`;
    const productValidationUrl = `${this.BASE_URL}/product/check-stock`;
    const getProductUrl = `${this.BASE_URL}/product/one/`;
    const updateProductUrl = `${this.BASE_URL}/product/update`;
    const orderUrl = `${this.BASE_URL}/orderHistory`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    const orderHistory: orderHistoryProducts = {};
    try {
      // Check all product quantities
      for (const product of products) {
        try {
          const checkResponse = await lastValueFrom(
            this.http.post<any>(productValidationUrl, {
              productId: product.id,
              quantity: productMap[product.id].quantity
            })
          );
          if (!checkResponse.success) {
            throw new Error(`Product ${product.productName}: Does not have enough stock available`);
          }
        }
        catch (validationError: any) {
          // Catch backend BAD_REQUEST responses here
          const errorMessage =
            validationError.error?.message ||
            `Product ${product.productName}: Does not have enough stock available`;
          throw new Error(errorMessage);
        }
      }
  
      for (const product of products) {
        try {
          const response = await lastValueFrom(this.http.get<any>(`${getProductUrl}${product.id}`, {headers} ));
          response.quantity = response.quantity - productMap[product.id].quantity;
          const response2 = await lastValueFrom(this.http.put<any>(updateProductUrl, response))
          orderHistory[product.id] = productMap[product.id].quantity;
        }
        catch (error: any) {
          throw error;
        }
      }
      const body = {
        products: orderHistory
      }
      const response3 = await lastValueFrom(this.http.post<any>(`${orderUrl}`, body, {headers}))
      const response4 =  await lastValueFrom(this.http.put<any>(`${url}`, {}, {headers}));
  
      return { success: true, message: 'Checkout successful' };
    } catch (error: any) {
      // Propagate the error to the component
      throw new Error(error.message || 'Checkout failed');
    }
  }




}
