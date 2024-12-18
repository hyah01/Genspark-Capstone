import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private readonly authService: AuthService, private router: Router) {}

  async handleSubmit(){
    if (!this.email || !this.password) {
      this.errorMessage = "Email and Password is required";
      return;
    }

    // ADD PASS WORD CHECKER


    try {
      const response = await this.authService.login(this.email, this.password);
      if (response.statusCode === 200){
        localStorage.setItem('token', response.token)
        localStorage.setItem('role', response.role)
        const cartResponse = await this.authService.hasCart(this.email, response.token);
        console.log(cartResponse);
          if (cartResponse.message == 'false'){
            try{
            const cartResponse = await this.authService.addCart(this.email, response.token);
            if (cartResponse.statusCode === 200){
              this.router.navigateByUrl("/").then(() => 
                window.location.reload()
              )
            }
          } catch (error: any){
            this.showError("Problem Creating User Cart: " + error.message)
          }
          } else {
            this.router.navigateByUrl("/").then(() => 
              window.location.reload()
          )
          }
          


      } else {
        this.showError(response.message)
      }

    } catch (error: any){
      this.showError(error.message)
    }
  }

  showError(message: string) {
    this.errorMessage = message;
    setTimeout(() => {
      this.errorMessage = ''
    }, 3000)
  }

}
