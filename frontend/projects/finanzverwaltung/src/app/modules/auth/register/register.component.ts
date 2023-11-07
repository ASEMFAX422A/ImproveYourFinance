import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  templateUrl: './register.component.html',
  styleUrls: ['../style.component.scss']
})
export class RegisterComponent {
  protected registerObject: any = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService) {}

  public sendForm() {
    this.authService.register(this.registerObject).then(x => {
      alert("User erstellt.");
    });
  }
}
