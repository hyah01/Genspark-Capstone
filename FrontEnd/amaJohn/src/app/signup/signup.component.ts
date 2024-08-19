import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { HttpClient } from '@angular/common/http';

import { error } from 'console';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {

  firstname: string = '';
  lastname: string = '';
  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router, private http: HttpClient) {}

  signup(form: NgForm) {
    if (form.valid) {
      const user = {
        first_name: this.firstname,
        last_name: this.lastname,
        email: this.email,
        password: this.password,
        role: 'USER',
        reward_points: 0,
        orderHistory_ids: []
      };

      if (!this.authService.checkEmail(this.email)){
        this.authService.signup(user).subscribe(
          response => {
            console.log('Sign up successful!', response);
            this.router.navigate(['/login']);
          },
          error => {
            console.error('Sign up unsuccessful!', error);
          }
        );
      } else {
        console.log('Email is exist');
      }
      
      
    } else {
      console.log('Form is invalid');
    }
    
  
    
  }
}
