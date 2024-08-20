import { Component } from '@angular/core';
import { Product } from '../models/product.model';
import { ActivatedRoute } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent {
  product: any;
  error: any;
  selectedImage: string = "";

  constructor(private route: ActivatedRoute, private service: ProductServiceService ){}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.service.getProductById(id).subscribe(data => {
        this.product = data && !data.hasOwnProperty('Error') ? data : null;
        this.error = this.product ? null : "404: Incorrect ID";
        this.selectedImage = this.product ? this.product.image[0] : "";
      });
    }
  }

  goBack() {
    window.history.back();
  }

  selectImage(image: string): void {
    this.selectedImage = image;
  }
  

}
