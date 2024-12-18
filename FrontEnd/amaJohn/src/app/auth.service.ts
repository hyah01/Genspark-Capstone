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
    console.log(userData)
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
  async getphoto(filename: string): Promise<Blob>{
    const url = `${this.BASE_URL}/photo/${filename}`;
    const headers = new HttpHeaders({
    })
    try {
      // Ensure responseType is explicitly set to 'blob'
      const response = await this.http.get(url, { headers, responseType: 'blob' as 'json' }).toPromise();
      return response as Blob;
    } catch (error) {
      console.error('Error fetching photo:', error);
      throw error;
    }
  }

  async uploadProfileImage(token:string,formData: FormData) {
    const url = `${this.BASE_URL}/users/adminuser/uploadProfileImage`;
    console.log(formData)
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`)
    
    try {
      console.log(headers.get('Authorization'))
      const response = await this.http.put<any>(url, formData, { headers }).toPromise();
      console.log("failed")
      return response;
    } catch (error) {
      
      throw error;
    }
  }

  async getUser(token:string){
    const url = `${this.BASE_URL}/users/get-User`;
    const headers = new HttpHeaders({
      'Authorization':`Bearer ${token}`
    })
    try {
      const response = this.http.get<any>(url, {headers} ).toPromise()
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
