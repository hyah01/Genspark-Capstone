import { Component } from '@angular/core';
import { Product } from '../models/product.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent {
  product: any;
  error: any;
  selectedImage: string = "";
  faStar = faStar;
  stars = [1, 2, 3, 4, 5];
  rating = 0;
  selectedQuantity: number = 1; // Default selected quantity
  quantities: number[] = Array.from({ length: 15 }, (_, i) => i + 1); // Array of quantities [1, 2, ..., 15]

  constructor(private route: ActivatedRoute, private service: ProductServiceService, private auth: AuthService, private router: Router){}
  

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.service.getProductById(id).subscribe(data => {
        this.product = data && !data.hasOwnProperty('Error') ? data : null;
        this.error = this.product ? null : "404: Incorrect ID";
        this.selectedImage = this.product ? this.product.image[0] : "";
        this.rating = 0 // TO DO 
      });
    }
  }

  addToCart(quanity: number){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.auth.addToUserCart(token,this.product.id, quanity);
      this.router.navigate(["/cart"])
    } 
    catch (error: any) {
      throw new Error(error)
    }
  }

  goBack() {
    window.history.back();
  }

  selectImage(image: string): void {
    this.selectedImage = image;
  }

  formatPrice(price: number): string {
    return price.toFixed(2);
  }
  

}
