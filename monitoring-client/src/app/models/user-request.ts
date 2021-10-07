import { Token } from './token';
import { UserRole } from './user-role';

export class UserRequest {
  constructor(public login: string, public password: string, public role: UserRole) {}
}
