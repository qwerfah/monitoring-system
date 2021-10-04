import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Credentials } from '../models/credentials';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  login(creds: Credentials) {}

  logout() {}

  refresh() {}

  register() {}
}
