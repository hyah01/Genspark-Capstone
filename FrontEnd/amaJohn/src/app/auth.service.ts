import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  
  private BASE_URL = "http://localhost:8082"
  private loggedIn = new BehaviorSubject<boolean>(false);
  private userDetails = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient, private route: Router) {}

  

  async login(email: string, password:string): Promise<any>{
    const url = `${this.BASE_URL}/auth/login`
    try {
      const response = this.http.post<any>(url, {email, password} ).toPromise()
      return response;
    } catch (error) {
      throw error;
    }
  }

  async signup(userData: any, token:string): Promise<any>{
    const url = `${this.BASE_URL}/auth/signup`;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    })
    try {
      const response = this.http.post<any>(url, userData, {headers} ).toPromise()
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
      const response = this.http.get<any>(url, {headers} ).toPromise()
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
      const response = this.http.get<any>(url, {headers} ).toPromise()
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
      const response = this.http.delete<any>(url, {headers} ).toPromise()
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
      const response = this.http.put<any>(url, userData, {headers} ).toPromise()
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
      return this.http.get(`${this.BASE_URL}/users/email`, {
        params: { email: email}
      });
    }

  // signup(user: any) {
  //   const headers = new HttpHeaders({
  //     'Content-Type': 'application/json',
  //   });

  //   return this.http.post(`${this.BASE_URL}/auth/signup`, user, {
  //     headers: headers,
  //     withCredentials: true,
  //     observe: 'response', // Observe response to get more detailed info
  //   });
  // }

  // login(user: any) {
  //   const headers = new HttpHeaders({
  //     'Content-Type': 'application/json',
  //   });

  //   return this.http.post(`${this.BASE_URL}/auth/login`, user, {
  //     headers: headers,
  //     withCredentials: true,
  //     observe: 'response', // Observe response to get more detailed info
  //   });
  // }

  // logout(): Observable<any> {
  //   return this.http.post(`${this.BASE_URL}/auth/logout`, {}, {
  //     withCredentials: true,
  //     observe: 'response',
  //   }).pipe(
  //     map(() => {
  //       this.loggedIn.next(false);
  //       this.userDetails.next(null);
  //     })
  //   );
  // }

  // checkEmail(email: string) {
  //   return this.http.get(`${this.BASE_URL}/users/email`, {
  //     params: { email: email}
  //   });
  // }

  // jwtHeader(): HttpHeaders {
  //   let jwt = localStorage.getItem('jwt');
  //   let headers = new HttpHeaders();
  //   return headers.set('Authorization', 'Bearer' + jwt)
  // }

  // verifyToken(): Observable<any> {
  //   return this.http.get(`${this.BASE_URL}/auth/verify-token`, {
  //     withCredentials: true, // ensure cookies are sent with request
  //     observe: 'response'
  //   }).pipe(
  //     map((response: any) => {
  //       if (response.status === 200) {
  //         this.loggedIn.next(true);
  //         this.userDetails.next(response.body);
  //         return response.body;
  //       } else {
  //         this.loggedIn.next(false);
  //         this.userDetails.next(null);
  //         return null
  //       }
  //     })
  //   )
  // }

  // isLoggedIn(): Observable<boolean> {
  //   return this.loggedIn.asObservable();
  // }

  // getUserDetails(): Observable<any> {
  //   return this.userDetails.asObservable();
  // }

  // verifyUser(): Observable<any> {
  //   return this.http.get(`${this.BASE_URL}/auth/verify`, {withCredentials: true});
  // }

}
