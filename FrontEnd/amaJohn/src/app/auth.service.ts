import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  
  private baseUrl = "http://localhost:8082"
  private loggedIn = new BehaviorSubject<boolean>(false);
  private userDetails = new BehaviorSubject<any>(null);

  constructor(private http: HttpClient, private route: Router) {}

  signup(user: any) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.baseUrl}/auth/signup`, user, {
      headers: headers,
      withCredentials: true,
      observe: 'response', // Observe response to get more detailed info
    });
  }

  login(user: any) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.baseUrl}/auth/login`, user, {
      headers: headers,
      withCredentials: true,
      observe: 'response', // Observe response to get more detailed info
    });
  }

  logout(): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/logout`, {}, {
      withCredentials: true,
      observe: 'response',
    }).pipe(
      map(() => {
        this.loggedIn.next(false);
        this.userDetails.next(null);
      })
    );
  }

  checkEmail(email: string) {
    return this.http.get(`${this.baseUrl}/users/email`, {
      params: { email: email}
    });
  }

  jwtHeader(): HttpHeaders {
    let jwt = localStorage.getItem('jwt');
    let headers = new HttpHeaders();
    return headers.set('Authorization', 'Bearer' + jwt)
  }

  verifyToken(): Observable<any> {
    return this.http.get(`${this.baseUrl}/auth/verify-token`, {
      withCredentials: true, // ensure cookies are sent with request
      observe: 'response'
    }).pipe(
      map((response: any) => {
        if (response.status === 200) {
          this.loggedIn.next(true);
          this.userDetails.next(response.body);
          return response.body;
        } else {
          this.loggedIn.next(false);
          this.userDetails.next(null);
          return null
        }
      })
    )
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  getUserDetails(): Observable<any> {
    return this.userDetails.asObservable();
  }

}
