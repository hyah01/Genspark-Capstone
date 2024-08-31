import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {
  userInfo: any;
  cart: any;

  constructor(private readonly auth: AuthService, private router: Router){}

  ngOnInit(): void {
    try {
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }

      this.auth.getUserProfile(token).then(response => {
        this.userInfo = response.user;
      });
      //console.log(this.userInfo);
    } catch (error: any) {
      throw new Error(error)
    }
  }

}
