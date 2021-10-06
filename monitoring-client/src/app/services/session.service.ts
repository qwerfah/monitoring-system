import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';

import { Credentials } from '../models/credentials';
import { User } from '../models/user';
import { Token } from '../models/token';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User>;

  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  /** Login in gateway service with given credentials. Recieved user data is stored in local storage.
   * @param creds User credentials.
   * @returns Observable instance with logged in user instance contains new access and refresh tokens.
   */
  login(creds: Credentials): Observable<User> {
    return this.http
      .post<User>(`${this.gatewayUri}/api/session/login`, { params: { login: creds.login, password: creds.password } })
      .pipe(
        map((user) => {
          if (user && user.token && user.token.access) {
            localStorage.setItem('currentUser', JSON.stringify(user));
            this.currentUserSubject.next(user);
          }

          return user;
        })
      );
  }

  /** Refresh access and refresh tokens with refresh token for current logged in user.
   * @returns Observable with new user tokens.
   */
  refresh(): Observable<Token> {
    return this.http
      .post<Token>('${this.gatewayUri}/api/session/refresh', null, {
        headers: { Authorization: `Bearer ${this.currentUserSubject.value?.token?.refresh}` },
      })
      .pipe(tap((tokens) => this.updateTokens(tokens)));
  }

  /** Remove current user data from local storage. */
  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  /** Register new user using gateway service. Doesn't login
   * created user and doesn't update current user data in local storage.
   * @param user New user data.
   * @returns Observable with registered user data.
   */
  register(user: User): Observable<User> {
    return this.http.post<User>('${this.gatewayUri/session/register}', user);
  }

  /** Update user tokens for user in local storage.
   * @param token New access and refresh tokens.
   */
  private updateTokens(token: Token) {
    let user = this.currentUserSubject.value;
    if (user != null) {
      user.token = token;
      localStorage.setItem('currentUser', JSON.stringify(user));
    }
  }
}
