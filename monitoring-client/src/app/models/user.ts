import { Token } from './token';
import { UserRole } from './user-role';

export class User {
  constructor(public uid: string, public login: string, public role: UserRole) {}
}

export class UserWithToken {
  constructor(public uid: string, public login: string, public role: UserRole, public token: Token) {}
}
