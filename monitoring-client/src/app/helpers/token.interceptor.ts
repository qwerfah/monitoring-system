import { Inject, Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, switchMap, filter, take, finalize } from 'rxjs/operators';
import { Router } from '@angular/router';
import { logError } from './log-error.operator';
import { SessionService } from '../services/session.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(
    private sessionService: SessionService,
    private router: Router,
    @Inject('GATEWAY_URI') private gatewayUri: string
  ) {}

  /**
   * Add auth header with jwt if user is logged in and request is to api url.
   */
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const currentUser = this.sessionService.currentUser;
    const isLoggedIn = currentUser && currentUser.token.access;
    const isBasePath = request.url.startsWith(this.gatewayUri);
    const isNotRefresh = !request.url.includes(`${this.gatewayUri}/api/session/refresh`);

    if (isLoggedIn && isBasePath && isNotRefresh) {
      request = this.addAccessToken(request, currentUser.token.access);
    }

    return next.handle(request).pipe(
      catchError((error) => {
        if (error.status === 401 && isNotRefresh) {
          return this.handle401Error(request, next);
        } else {
          return throwError(error);
        }
      }),
      logError
    );
  }

  private addAccessToken(request: HttpRequest<any>, accessToken: string) {
    return request.clone({
      setHeaders: { Authorization: `Bearer ${accessToken}` },
    });
  }

  // We want to queue all HTTP requests in case of refreshing.
  // This means that if the server responds with 401 Error, we want to start refreshing,
  // block all requests that may happen during refreshing,
  // and release them once refreshing is done. To be able to block and release requests
  // during the refreshing, we will use BehaviorSubject as a semaphore.
  private refreshTokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);
  private isRefreshing = false;

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.sessionService.refresh().pipe(
        switchMap((tokens) => {
          this.refreshTokenSubject.next(tokens.access);
          return next.handle(this.addAccessToken(request, tokens.access));
        }),
        logError,
        catchError((error) => {
          this.sessionService.logout();
          this.router.navigate(['/login'], { queryParams: { returnUrl: this.router.routerState.snapshot.url } });
          return throwError(error.error);
        }),
        finalize(() => (this.isRefreshing = false))
      );
    } else {
      return this.refreshTokenSubject.pipe(
        filter((accessToken) => accessToken != null),
        take(1),
        switchMap((accessToken) => {
          if (accessToken == null) throw new ReferenceError('No access token');
          return next.handle(this.addAccessToken(request, accessToken));
        }),
        logError
      );
    }
  }
}
