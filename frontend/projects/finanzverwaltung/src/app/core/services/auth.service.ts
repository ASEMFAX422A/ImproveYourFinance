import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { RequestService } from './request.service';
import { Observable, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly jwtToken : string = 'jwtToken';
  private jwtHelper: JwtHelperService;

  constructor(private requestService: RequestService) {
    this.jwtHelper = new JwtHelperService();
  }

  getToken(): string {
    return localStorage.getItem(this.jwtToken) ?? '';
  }

  setToken(token: string): void {
    localStorage.setItem(this.jwtToken, token);
  }

  deleteToken(): void {
    localStorage.removeItem(this.jwtToken);
  }

  logout() : void {
    this.deleteToken();
  }


  isAuthenticated(): boolean {
    const token = this.getToken();

    //isTokenExpired prüft selbst, ob token leer ist
    return !this.jwtHelper.isTokenExpired(token);
  }

  login(loginInfo: { username: string; password: string }) : Promise<boolean> {
    if (this.isAuthenticated()) {
      return Promise.resolve(false);
    }

    return new Promise((resolve, reject) => {
      this.requestService.post("auth/login", loginInfo).subscribe((response: any) => {
        this.setToken(response.token);
        resolve(this.isAuthenticated());
      });
    });
  }

  register(registrationInfo: { username: string; password: string }) : Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.requestService.post("auth/registration", registrationInfo)
      .subscribe((response: any) => {
        resolve(response.status == 200);
      });
    });
  }
}
