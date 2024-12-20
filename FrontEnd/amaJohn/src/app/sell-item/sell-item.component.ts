import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { ActivatedRoute } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';

@Component({
  selector: 'app-sell-item',
  templateUrl: './sell-item.component.html',
  styleUrl: './sell-item.component.css'
})
export class SellItemComponent {
  constructor(private readonly auth: AuthService, private route: ActivatedRoute, private service: ProductServiceService ){}
  token: string = "";
  formData: any = {
    productName: '',
    price: 0,
    currency: 'USD',
    Quantity: 0,
    Discount: 0,
    categories: [],
    productDesc: '',
    images: []
  };

  ngOnInit(): void {
    try {
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }

      
      this.token = token
      this.auth.getUser(this.token);
    } catch (error: any) {
      throw new Error(error)
    }
  }

  async onSubmit(formData:any){
    console.log("Adding product")
    await this.auth.addProduct(this.token,formData);
    console.log("Product added")
  }

}
