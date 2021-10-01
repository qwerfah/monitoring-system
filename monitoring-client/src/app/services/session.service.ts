import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Credentials } from '../models/credentials';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HeroService {
  constructor(private http: HttpClient, @Inject('SESSION_URI') private sessionUri: string) {}

  login(creds: Credentials) {}

  logout() {}

  refresh() {}

  register() {}
}
