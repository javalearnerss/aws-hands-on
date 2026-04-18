import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private api = "/api/users/register";

  constructor(private http: HttpClient) {}

  registerUser(user: User): Observable<any> {
    return this.http.post(this.api, user);
  }

}