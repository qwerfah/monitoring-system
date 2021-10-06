import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { SessionService } from '../services/session.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private sessionService: SessionService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const currentUser = this.sessionService.currentUser;

    if (currentUser) {
      if (route.data.roles && route.data.roles.indexOf(currentUser.role) === -1) {
        this.router.navigate(['/']);
        return false;
      }

      return true;
    }

    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });

    return false;
  }
}
