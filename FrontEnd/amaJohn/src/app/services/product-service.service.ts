import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductServiceService {
  private apiurl = "http://localhost:8080"

  constructor(private http: HttpClient) { }

  getProducts(): Observable<any[]>{
    return this.http.get<any[]>(this.apiurl)
  }

}
