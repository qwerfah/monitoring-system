import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

import { Credentials } from '../../models/credentials';
import { SessionService } from 'src/app/services/session.service';
import { first } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponseBase } from '@angular/common/http';

@Component({
  selector: 'authorization-form',
  templateUrl: './authorization-form.component.html',
  styleUrls: ['./authorization-form.component.css'],
})
export class AuthorizationFormComponent implements OnInit {
  credentials: FormGroup;
  returnUrl: string;
  isLoading: Boolean = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private sessionService: SessionService,
    private snackBar: MatSnackBar
  ) {
    this.credentials = fb.group({
      login: [null, [Validators.required]],
      password: [
        null,
        //[Validators.required, Validators.minLength(6), Validators.pattern('^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$')],
        [Validators.required],
      ],
    });

    if (this.sessionService.currentUser) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  submit(): void {
    if (this.credentials.invalid) {
      this.credentials.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    let creds = new Credentials(this.credentials.controls.login.value, this.credentials.controls.password.value);

    console.log(`Email: ${this.credentials.controls.login.value}`);
    console.log(`Password: ${this.credentials.controls.password.value}`);

    this.sessionService.login(creds).subscribe(
      (user) => {
        this.router.navigate([this.returnUrl]);
      },
      (error: HttpErrorResponse) => {
        this.isLoading = false;
        switch (error.status) {
          case 0: {
            this.snackBar.open('Ошибка авторизации: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка авторизации: сервис авторизации недоступен', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка авторизации: неверные учетные данные', 'Ок');
          }
        }
      }
    );
  }

  reset(): void {}
}
