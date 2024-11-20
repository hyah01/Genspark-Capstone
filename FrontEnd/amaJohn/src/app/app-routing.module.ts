import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';

import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { ProductsComponent } from './products/products.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { userGuard } from './auth.guard';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  {path:"login", component: LoginComponent},
  {path:"register", component: SignupComponent},
  {path: "products", component: ProductsComponent},
  {path: 'cart', component: ShoppingCartComponent, canActivate: [userGuard]},
  {path: 'products/:id', component: ProductDetailComponent},
  {path:"", component: HomepageComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path:'profile',component:ProfileComponent, canActivate: [userGuard]}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
