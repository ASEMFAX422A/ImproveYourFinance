import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  protected loginObject: any = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService) {}

  public sendForm() {
    this.authService.login(this.loginObject);
  }
}
