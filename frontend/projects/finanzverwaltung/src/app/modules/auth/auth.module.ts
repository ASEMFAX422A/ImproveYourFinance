import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule, Routes } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { MaterialModule } from '../../material/material.module';


import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

import { authGuardLogin } from '../../core/guards/auth.guard';
import { FooterComponent } from '../../extras/footer/footer.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent,canActivate:[authGuardLogin]},
  { path: 'register', component: RegisterComponent, canActivate:[authGuardLogin]}
];

@NgModule({
  declarations: [
    RegisterComponent,
    LoginComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      preventDuplicates: true,
      positionClass: 'toast-top-right',
      enableHtml: true
    }),
    RouterModule.forChild(routes),
    MaterialModule,
    FooterComponent
  ]
})
export class AuthModule { }
