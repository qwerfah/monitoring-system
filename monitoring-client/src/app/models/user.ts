import { Token } from "./token";

export class User {
  constructor(public uid: string, public login: string, public token: Token | null) {}
}
