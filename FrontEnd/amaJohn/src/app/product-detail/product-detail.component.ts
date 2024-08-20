import { Component } from '@angular/core';
import { Product } from '../models/product.model';
import { ActivatedRoute } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { faStar } from '@fortawesome/free-solid-svg-icons';

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

  constructor(private route: ActivatedRoute, private service: ProductServiceService ){}
  

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
