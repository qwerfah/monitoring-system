import { SessionService } from '../services/session.service';

import { UserWithToken } from '../models/user';
import { UserRole } from '../models/user-role';

/** Provide actions for visualization of user-dependent component. */
export class UserDependentComponent {
  UserRole = UserRole;

  currentUser: UserWithToken | undefined = undefined;

  constructor(protected sessionService: SessionService) {
    this.sessionService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  /** Check if current logged in user has sufficient rights to access element.
   * @param roles Array of sufficient user roles.
   * @returns True if current user role is sufficient, otherwise false.
   */
  isAllowed(roles: UserRole[]): boolean {
    return this.currentUser !== undefined && roles.indexOf(this.currentUser.role) !== -1;
  }
}
