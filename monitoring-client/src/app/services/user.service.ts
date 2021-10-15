import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Message } from '../models/message';

import { User } from '../models/user';
import { UserRequest } from '../models/user-request';
import { ServiceBase } from './service.base';

@Injectable({
  providedIn: 'root',
})
export class UserService extends ServiceBase {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {
    super();
  }

  getUsers(snackBar: MatSnackBar | null = null): Observable<User[]> {
    return this.http
      .get<User[]>(`${this.gatewayUri}/api/session/users`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  /** Register new user using gateway service. Doesn't login
   * created user and doesn't update current user data in local storage.
   * @param user New user data.
   * @returns Observable with registered user data.
   */
  register(user: UserRequest, snackBar: MatSnackBar | null = null): Observable<User> {
    return this.http
      .post<User>(`${this.gatewayUri}/api/session/users/register`, user)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  addUser(user: UserRequest, snackBar: MatSnackBar | null = null): Observable<User> {
    return this.http
      .post<User>(`${this.gatewayUri}/api/session/users`, user)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  removeUser(userUid: string, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .delete<Message>(`${this.gatewayUri}/api/session/users/${userUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }
}
