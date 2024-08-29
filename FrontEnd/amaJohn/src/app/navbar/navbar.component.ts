import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router, RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  isAuthenticated: boolean = false;
  isAdmin:boolean = false;
  isUser:boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void{ 
    this.isAuthenticated = this.authService.isAuthenicated();
    this.isAdmin = this.authService.isAdmin();
    this.isUser = this.authService.isUser();
  }

  logout():void {
    this.authService.logOut();
    this.isAuthenticated = false;
    this.isAdmin = false;
    this.isUser = false;
  }

}
