import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {

  toTop(): void{
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'instant'
    })
  }
}
