import { Component } from '@angular/core';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'amaJohn';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.verifyToken().subscribe();
  }
}
