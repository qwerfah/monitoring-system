import { Component, OnInit } from '@angular/core';
import { AuthorizationModule } from './authorization.module';

@Component({
  selector: 'app-authorization',
  templateUrl: './authorization.component.html',
  styleUrls: ['./authorization.component.css']
})
export class AuthorizationComponent implements OnInit {

  email: string = '';
  password: string = '';

  constructor() { }

  ngOnInit(): void {
  }

  login(): void {
    console.log(`Email: ${ this.email }`);
    console.log(`Password: ${ this.password }`);
  }

}
