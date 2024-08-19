import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnDestroy, Renderer2, RendererFactory2 } from '@angular/core';
import { Router } from '@angular/router';
import { ProductServiceService } from '../services/product-service.service';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnDestroy {
  query: string = '';
  items: string[][] = []; 
  filteredItems: string[][] = [];
  isDropdownOpen: boolean = false;

  private renderer: Renderer2;
  private documentClickListener: () => void;

  constructor(private elementRef: ElementRef, rendererFactory: RendererFactory2, private router: Router, private productService: ProductServiceService) {
    this.renderer = rendererFactory.createRenderer(null, null);
    // Listen for clicks outside the component
    this.documentClickListener = this.renderer.listen('document', 'click', this.onDocumentClick.bind(this));
  }

  ngOnInit(){
    this.productService.getProducts().subscribe(data => {
      this.items = data.map(product => [product.productName, product.image[0]]);
    })
  }

  ngOnDestroy() {
    // Clean up event listener when component is destroyed
    if (this.documentClickListener) {
      this.documentClickListener();
    }
  }

  onSearch() {
    // Check if there is a query, if not, there is no items
    if (this.query == ''){
      this.filteredItems = []
    } else {
      // else filter items and return
      this.filteredItems = this.items.filter(item => 
        item[0].toLowerCase().includes(this.query.toLowerCase())
      );
    }
    this.isDropdownOpen = this.filteredItems.length > 0
  }

  selectItem(item: string) {
    this.query = item;
    this.filteredItems = [];
    this.isDropdownOpen = false;
    this.router.navigate(['/products'], { queryParams: { search: this.query } });
    this.query = '';
  }

  closeDropdown() {
    this.filteredItems = [];
    this.isDropdownOpen = false;
  }

  onSearchBarClick(event: MouseEvent) {
    // When click on search bar
    this.onSearch();
    event.stopPropagation();
  }

  onDocumentClick(event: MouseEvent) {
    // when click outside search bar
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.closeDropdown();
    }
  }
  onSearchButtonClick() {
    if (!(this.query == '')){
      this.router.navigate(['/products'], { queryParams: { search: this.query } });
      this.query = '';
      this.filteredItems = [];
    }

  }
}