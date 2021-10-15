import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ServiceBase } from './service.base';

import { Credentials } from '../models/credentials';
import { UserWithToken } from '../models/user';
import { Token } from '../models/token';

@Injectable({
  providedIn: 'root',
})
export class SessionService extends ServiceBase {
  private currentUserSubject: BehaviorSubject<UserWithToken | undefined>;
  public currentUser$: Observable<UserWithToken | undefined>;

  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {
    super();
    let user = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<UserWithToken | undefined>(user ? JSON.parse(user) : undefined);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  /** Login in gateway service with given credentials. Recieved user data is stored in local storage.
   * @param creds User credentials.
   * @returns Observable instance with logged in user instance contains new access and refresh tokens.
   */
  login(creds: Credentials, snackBar: MatSnackBar | null = null): Observable<UserWithToken> {
    return this.http
      .post<UserWithToken>(`${this.gatewayUri}/api/session/login`, creds)
      .pipe(catchError(this.baseErrorHandler(snackBar)))
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
    if (this.currentUser === undefined) throw new ReferenceError('No authorized user');
    return this.http
      .post<Token>(`${this.gatewayUri}/api/session/refresh`, null, {
        headers: { Authorization: `Bearer ${this.currentUser.token?.refresh}` },
      })
      .pipe(tap((tokens) => this.updateTokens(tokens)));
  }

  /** Remove current user data from local storage. */
  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(undefined);
  }

  /** Exctract current user or throw error if it is not presented. */
  get currentUser(): UserWithToken | undefined {
    return this.currentUserSubject.value;
  }

  /** Update user tokens for user in local storage.
   * @param token New access and refresh tokens.
   */
  private updateTokens(token: Token): void {
    let user = this.currentUser;
    if (user === undefined) throw new ReferenceError('No authorized user');
    user.token = token;
    localStorage.setItem('currentUser', JSON.stringify(user));
  }
}
