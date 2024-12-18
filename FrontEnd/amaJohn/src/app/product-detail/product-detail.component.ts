import { Component } from '@angular/core';
import { Product } from '../models/product.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../auth.service';
import { CartServiceService } from '../services/cart-service.service';

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
  selectedQuantity: number = 1; 
  // array for user to select how many item they want
  quantities: number[] = [];

  constructor(private route: ActivatedRoute, private service: ProductServiceService, private auth: AuthService, private router: Router, private cartService: CartServiceService){}
  

  ngOnInit(): void {
    // Get Specific item ID
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.service.getProductById(id).subscribe(data => {
        this.product = data && !data.hasOwnProperty('Error') ? data : null;
        // If ID don't exist in data base, we will show error page
        this.error = this.product ? null : "404: Incorrect ID";
        this.selectedImage = this.product ? this.product.image[0] : "";
        this.rating = 0; // TO DO 
        // If more than 15 product are available then show up to only 15, if not then show up to how many it has
        if (this.product.quantity < 16){
          this.quantities = Array.from({ length: this.product.quantity }, (_, i) => i + 1); 
        } else {
          this.quantities = Array.from({ length: 15 }, (_, i) => i + 1); 
        }
      });
    }
  }

  addToCart(quanity: number){
    try{
      const token = localStorage.getItem('token');
      if (!token){
        throw new Error('No Token Found')
      }
      this.cartService.addToUserCart(token,this.product.id, quanity);
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
