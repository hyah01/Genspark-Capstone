import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.css'
})
export class HomepageComponent {
  items: Product[] = []; 

  constructor(private router: Router, private productService: ProductServiceService){}

  ngOnInit(){
    this.productService.getProducts().subscribe(data => {
      this.items = data;
    })
  }

  selectItem(productId: string){
    this.router.navigateByUrl(`/db/${productId}`).then(() => {
      window.location.reload();
    });
  }
}
