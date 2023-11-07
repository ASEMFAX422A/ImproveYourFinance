import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

import { authGuardLogin } from '../../core/guards/auth.guard';
import { MaterialModule } from '../../material/material.module';


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
    RouterModule.forChild(routes),
    MaterialModule

  ]
})
export class AuthModule { }
