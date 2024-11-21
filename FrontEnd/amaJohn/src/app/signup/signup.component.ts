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
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router, private http: HttpClient) {}

  async signup(form: NgForm) {
    if (form.valid) {
      const user = {
        first_name: this.firstname,
        last_name: this.lastname,
        email: this.email,
        password: this.password,
        role: 'USER',
        reward_points: 0,
        orderHistory_ids: [],
        image:'download.png'
      };

      this.authService.checkEmail(this.email).subscribe(async emailExists => {
        if (!emailExists) {
          try {
            const response = await this.authService.signup(user);
            if (response.statusCode === 200){
              return response.then(this.router.navigate(["/login"]));
            } else {
              this.showError(response.message);
            }

          } catch (error: any) {
            if (error.error instanceof ErrorEvent) {
                this.showError(error.error.message);
            } else {
                // Handle non-JSON response 
                try {
                    const errorText = await error.text();
                    this.showError(errorText);
                } catch (e) {
                    this.showError('Unknown error occurred');
                }
            }
        }
        } else {
          this.showError('Email already exists');
          return
        }
      });
    } else {
      if (form.controls['email'].invalid) {
        this.showError('Please enter a valid email address');
      } else {
        this.showError('Please fill in all fields');
      }
      
      return
    }
  }

  showError(message: string) {
    this.errorMessage = message;
    setTimeout(() => {
      this.errorMessage = ''
    }, 3000)
  }
}