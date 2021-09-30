import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { login, logout } from '../authorization.actions';
import { Credentials } from '../../models/credentials';
import { first } from 'rxjs/operators';

@Component({
  selector: 'authorization-form',
  templateUrl: './authorization-form.component.html',
  styleUrls: ['./authorization-form.component.css'],
})
export class AuthorizationFormComponent implements OnInit {
  credentials: FormGroup;
  user$: Observable<Credentials>;

  constructor(private fb: FormBuilder, private store: Store<{ user: Credentials }>) {
    this.credentials = fb.group({
      login: [null, [Validators.required]],
      password: [
        null,
        [Validators.required, Validators.minLength(6), Validators.pattern('^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$')],
      ],
    });

    this.user$ = store.select('user');
  }

  ngOnInit(): void {}

  submit(): void {
    if (this.credentials.invalid) {
      this.credentials.markAllAsTouched();
    } else {
      let creds = new Credentials(this.credentials.controls.login.value, this.credentials.controls.password.value);

      console.log(`Email: ${this.credentials.controls.login.value}`);
      console.log(`Password: ${this.credentials.controls.password.value}`);
      this.store.dispatch(login({ creds }));
      this.user$.pipe(first()).subscribe((data) => console.log(data.login));
    }
  }

  reset(): void {
    this.store.dispatch(logout());
    this.user$.pipe(first()).subscribe(
      (data) => {
        console.log(data);
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
