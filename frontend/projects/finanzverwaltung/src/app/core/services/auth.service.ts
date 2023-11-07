import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { RequestService } from './request.service';
import { Observable, catchError, of, throwError } from 'rxjs';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly jwtToken : string = 'jwtToken';
  private jwtHelper: JwtHelperService;

  constructor(private requestService: RequestService,private route: Router,) {
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
    window.location.reload();
  }
  
  getUsername(): string {
    const decodedToken = this.jwtHelper.decodeToken(this.getToken());

    return decodedToken.sub;
  }

  
  isAuthenticated(): boolean {
    const token = this.getToken();
    try{
      //isTokenExpired prüft selbst, ob token leer ist
      return !this.jwtHelper.isTokenExpired(token);
    }catch(error){

    }
    return false;
  }

  login(loginInfo: { username: string; password: string }) : Promise<boolean> {
    if (this.isAuthenticated()) {
      return Promise.resolve(false);
    }

    return new Promise((resolve, reject) => {
      this.requestService.post("auth/login", loginInfo).pipe(catchError(error=>{
        resolve(false);
        return "";
      })).subscribe((response: any) => {
        if(response != ""){
          this.setToken(response.token);
        }
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
