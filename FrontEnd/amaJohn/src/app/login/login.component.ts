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

      } else {
        this.email
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



  // login() {
  //   const user = {
  //     email: this.email,
  //     password: this.password,
  //   };

  //   this.authService.login(user).subscribe(
  //     (response) => {
  //       console.log('Login successful!');
  //       this.router.navigateByUrl(`/`).then(() => {
  //         window.location.reload();
  //       });
  //     },
  //     (error) => {
  //       console.error('Login failed!', error);
       
  //     }
  //   );
  // }


}
