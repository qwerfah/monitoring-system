import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { User } from '../models/user';
import { UserRequest } from '../models/user-request';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.gatewayUri}/api/session/users`);
  }

  addUser(user: UserRequest): Observable<User> {
    return this.http.post<User>(`${this.gatewayUri}/api/session/users`, user);
  }
}
