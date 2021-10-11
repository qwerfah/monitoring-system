import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Message } from '../models/message';

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

  /** Register new user using gateway service. Doesn't login
   * created user and doesn't update current user data in local storage.
   * @param user New user data.
   * @returns Observable with registered user data.
   */
  register(user: UserRequest): Observable<User> {
    return this.http.post<User>(`${this.gatewayUri}/api/session/users/register`, user);
  }

  addUser(user: UserRequest): Observable<User> {
    return this.http.post<User>(`${this.gatewayUri}/api/session/users`, user);
  }

  removeUser(userUid: string): Observable<Message> {
    return this.http.delete<Message>(`${this.gatewayUri}/api/session/users/${userUid}`);
  }
}
