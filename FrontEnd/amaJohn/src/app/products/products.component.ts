import { Component, input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';
import { combineLatest, map } from 'rxjs';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent {
  searchQuery: string = '';
  products: any[] = [];
  filteredProducts: any[] = [];

  
  constructor(private route: ActivatedRoute, private productService: ProductServiceService) {}

  ngOnInit(): void {
    combineLatest([
      this.route.queryParams.pipe(map(params => params['search'] || '')),
      this.productService.getProducts()
    ]).subscribe(([searchQuery, products]) => {
      this.searchQuery = searchQuery;
      this.products = products;

      this.filteredProducts = this.products.filter(item => 
        item.productName.toLowerCase().includes(this.searchQuery.toLowerCase())
      )
    })

  }

  formatPrice(price: number): string {
    return price.toFixed(2);
  }





}
