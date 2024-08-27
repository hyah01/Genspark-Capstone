import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';

import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ProductsComponent } from './products/products.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';

const routes: Routes = [
  {path:"", component: HomepageComponent},
  {path:"login", component: LoginComponent},
  {path:"register", component: SignupComponent},
  {path: "products", component: ProductsComponent},
  {path: 'shopping-cart', component: ShoppingCartComponent},
  {path: 'products/:id', component: ProductDetailComponent},
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
