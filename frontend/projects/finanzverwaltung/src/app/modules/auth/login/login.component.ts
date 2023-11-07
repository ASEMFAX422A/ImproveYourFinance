import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['../style.component.scss']
})
export class LoginComponent {
  protected loginObject: any = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  public sendForm() {
    this.authService.login(this.loginObject).then(result=>{
      if(result){
        this.router.navigate(["/overview"]);
      }
      else{
        alert("Sorry bro");
      }

    });

  }
}
