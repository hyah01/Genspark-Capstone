import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, lastValueFrom, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  
  private BASE_URL = "http://localhost:8060";

  constructor(private http: HttpClient, private route: Router) {}

  

  async login(email: string, password:string): Promise<any>{
    const url = `${this.BASE_URL}/auth/login`
    try {
      const response = await lastValueFrom(this.http.post<any>(url, {email, password} ));
      return response;
    } catch (error) {
      throw error;
    }
  }

  async signup(userData: any): Promise<any>{
    const url = `${this.BASE_URL}/auth/signup`;
    const headers = new HttpHeaders({
          'Content-Type': 'application/json',
        });
    try {
      const response = await lastValueFrom(this.http.post<any>(url, userData, {headers} ));
      return response;
    } catch (error) {
      throw error;
    }
  }

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


  async hasCart(email: string, token: string):Promise<any>{
    const url = `${this.BASE_URL}/cart/has-cart/` + email;
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


  async getAllUsers(token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/get-all-users`;
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

  async getUserProfile(token:string): Promise<any>{
    const url = `${this.BASE_URL}/adminuser/get-profile`;
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

  async getUserById(userId: string, token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/get-user/${userId}`;
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

  async deleteUser(userId: string, token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/delete/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = await lastValueFrom(this.http.delete<any>(url, {headers} ));
      return response;
    } catch (error) {
      throw error;
    }
  }

  async updateUser(userId: string, userData: any, token:string): Promise<any>{
    const url = `${this.BASE_URL}/admin/update/${userId}`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = await lastValueFrom(this.http.put<any>(url, userData, {headers} ));
      return response;
    } catch (error) {
      throw error;
    }
  }

  logOut(){
    if (typeof localStorage !== 'undefined'){
      localStorage.removeItem('token');
      localStorage.removeItem('role');
    }
  }

  isAuthenicated(): boolean{
    if (typeof localStorage !== 'undefined'){
      const token = localStorage.getItem('token');
      return !!token;
    }
    return false;
  }

  isAdmin(): boolean{
    if (typeof localStorage !== 'undefined'){
      const role = localStorage.getItem('role');
      return role === 'ADMIN';
    }
    return false;
  }

  isUser(): boolean{
    if (typeof localStorage !== 'undefined'){
      const role = localStorage.getItem('role');
      return role === 'USER';
    }
    return false;
  }

  checkEmail(email: string) {
      return this.http.get(`${this.BASE_URL}/auth/email`, {
        params: { email: email}
      });
    }


}
