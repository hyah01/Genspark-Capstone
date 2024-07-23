import { Component } from '@angular/core';
import { AuthService } from '../auth.service';

import { error } from 'console';
import { Router } from '@angular/router';

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

  constructor(private authService: AuthService, private router: Router) {}

  

  signup() {
    const user = {
      firstName: this.firstname,
      lastName: this.lastname,
      email: this.email,
      password: this.password
    };
  
    this.authService.signup(user).subscribe(
      response => {
        console.log('Sign up successful!', response);
        this.router.navigate(['/login']);
      },
      error => {
        console.error('Sign up unsuccessful!', error);
      }
    );
  }
}
