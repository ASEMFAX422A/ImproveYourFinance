import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { RequestService } from './request.service';
import { catchError, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

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
      console.log(error);
    }
    return false;
  }

  login(loginInfo: { username: string; password: string }) : Promise<boolean | undefined> {
    if (this.isAuthenticated()) {
      return Promise.resolve(false);
    }

    return new Promise((resolve, reject) => {
      this.requestService.post("auth/login", loginInfo).pipe(catchError((error: HttpErrorResponse) => {
        resolve(error.status === 0 ? undefined : false);
        return of(false);
      })).subscribe((response: any) => {
        if (response && response.message != ""){
          this.setToken(response.message);
        }

        resolve(this.isAuthenticated());
      });
    });
  }

  register(registrationInfo: { username: string; password: string }) : Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.requestService.post("auth/registration", registrationInfo).pipe(catchError(error => {
        resolve(false);
        return of(false);
      })).subscribe((response: any) => {
        // response ist der HTTP Body
        resolve(true);
      });
    });
  }
}