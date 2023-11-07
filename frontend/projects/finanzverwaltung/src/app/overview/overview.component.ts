import { Component } from '@angular/core';
import { AuthService } from '../core/services/auth.service';
import { RegisterComponent } from '../modules/auth/register/register.component';
import { LoginComponent } from '../modules/auth/login/login.component';

@Component({
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent {
  constructor(private auth: AuthService) {}
  showFiller = false;
  username= this.auth.getUsername();

  
}
