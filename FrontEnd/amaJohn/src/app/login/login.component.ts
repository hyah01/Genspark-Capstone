import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  

  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    const user = {
      email: this.email,
      password: this.password,
    };

    this.authService.login(user).subscribe(
      (response) => {
        console.log('Login successful!', response);
        this.router.navigate(['/']); // Redirect to the dashboard or another route after successful login
      },
      (error) => {
        console.error('Login failed!', error);

      }
    );
  }


}
