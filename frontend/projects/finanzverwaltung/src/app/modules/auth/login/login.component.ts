import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['../auth.style.scss']
})
export class LoginComponent {
  protected loginObject: any = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) {}

  public sendForm() {
    this.authService.login(this.loginObject).then(result=>{
      if(result){
        this.router.navigate(["/overview"]);
        this.toastr.success('Erfolgreich eingeloggt!');
      }
      else{
        this.toastr.error('Logindaten ungültig!');
      }
    });
  }
}
