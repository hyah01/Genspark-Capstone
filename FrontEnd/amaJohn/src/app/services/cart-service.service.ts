import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, lastValueFrom, map } from 'rxjs';
import { Product } from '../models/product.model';

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

  async checkout(token: string, products: Product[]):Promise<any>{
    const url = `${this.BASE_URL}/cart-item/checkout`;
    const productValidationUrl = `${this.BASE_URL}/product/check-stock`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    try {
      // Check all product quantities
      for (const product of products) {
        try {
          const checkResponse = await lastValueFrom(
            this.http.post<any>(productValidationUrl, {
              productId: product.id,
              quantity: product.quantity
            })
          );
          if (!checkResponse.success) {
            throw new Error(`Product ${product.id} is out of stock`);
          }
        }
        catch (validationError: any) {
          // Catch backend BAD_REQUEST responses here
          const errorMessage =
            validationError.error?.message ||
            `Product '${product.id}' is out of stock`;
          throw new Error(errorMessage);
        }
      }
  
      // TODO : If all pass, finalize checkout logic
      // Reserve stock or confirm order logic here
  
      return { success: true, message: 'Checkout successful' };
    } catch (error: any) {
      // Propagate the error to the component
      throw new Error(error.message || 'Checkout failed');
    }
  }




}
