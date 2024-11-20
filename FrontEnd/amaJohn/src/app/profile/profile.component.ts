import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
    constructor(private readonly auth: AuthService, private route: ActivatedRoute, private service: ProductServiceService ){}
    user: any;
    ngOnInit(): void {
      try {
        const token = localStorage.getItem('token');
        if (!token){
          throw new Error('No Token Found')
        }
  
        this.getUser(token);
      } catch (error: any) {
        throw new Error(error)
      }
    }
    async getUser(token:string){
      this.user = (await this.auth.getUser(token))
    }
}
