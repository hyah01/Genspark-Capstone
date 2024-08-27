import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  loggedIn: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.isLoggedIn().subscribe(
      (status: boolean) => {
        this.loggedIn = status;
      }
    );

    //this.authService.verifyToken().subscribe();
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      console.log('User logged out');
      // Optionally redirect to login page or perform other actions
    });
  }
}
