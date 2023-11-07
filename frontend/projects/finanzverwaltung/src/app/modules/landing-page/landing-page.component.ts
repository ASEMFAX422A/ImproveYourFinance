import { Component } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  templateUrl: 'landing-page.component.html',
  styleUrls: ['landing-page.component.scss'],


})
export class LandingPageComponent {
  constructor(private auth: AuthService, private router: Router){}
  
  //landing-page button
  onClick(){
    if(this.auth.isAuthenticated()){

        this.router.navigate(["/overview"]);
    }
    else{
      this.router.navigate(["/login"]);
    }
  }
}
