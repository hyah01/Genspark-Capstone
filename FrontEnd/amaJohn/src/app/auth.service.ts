import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  baseUrl = "http://localhost:8060/auth"

  constructor(private http: HttpClient, private route: Router) {}

  signup(user:any) {
    return this.http.post(`${this.baseUrl}/signup`, user, {withCredentials: true});
  }
}
