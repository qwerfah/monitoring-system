import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { User } from 'src/app/models/user';
import { UserRequest } from 'src/app/models/user-request';
import { UserRole } from 'src/app/models/user-role';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css'],
})
export class RegistrationFormComponent implements OnInit {
  userInfo: FormGroup;

  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {
    this.userInfo = fb.group({
      login: [null, [Validators.required]],
      password: [
        null,
        [Validators.required, Validators.minLength(6), Validators.pattern('^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$')],
      ],
      confirmation: [
        null,
        [Validators.required, Validators.minLength(6), Validators.pattern('^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$')],
      ],
    });
  }

  ngOnInit(): void {}

  submit(): void {
    if (this.userInfo.invalid) {
      this.userInfo.markAllAsTouched();
    } else {
      let user = new UserRequest(
        this.userInfo.controls.login.value,
        this.userInfo.controls.password.value,
        UserRole.EquipmentUser
      );

      this.isLoading = true;

      this.userService.register(user, this.snackBar).subscribe(() => {
        this.snackBar.open('Успех: пользователь зарегистрирован', 'Ок', { duration: 5000 });
        this.router.navigate(['/login']);
      });
    }
  }
}
