import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  private readonly endpoint: string = "http://localhost:8080/api/v1/";

  constructor(private http: HttpClient) {}
  
  public post(url: string, body: any, options? : any) : Observable<any>{
    return this.http.post(this.endpoint + url, body, options);
  }
}
