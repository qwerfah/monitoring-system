import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { UserRequest } from 'src/app/models/user-request';
import { UserRole } from 'src/app/models/user-role';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css'],
})
export class CreateUserComponent implements OnInit {
  @Output() addEvent = new EventEmitter<UserRequest | null>();

  userForm: FormGroup;

  role: UserRole | undefined = undefined;

  UserRole = UserRole;

  isInvalid: boolean = false;

  userRoles: Map<UserRole, string> = new Map([
    [UserRole.EquipmentUser, 'Пользователь оборудования'],
    [UserRole.EquipmentAdmin, 'Администратор оборудования'],
    [UserRole.SystemAdmin, 'Администратор системы'],
  ]);

  constructor(private fb: FormBuilder) {
    this.userForm = fb.group({
      login: [null, [Validators.required], Validators.minLength(3)],
      password: [
        null,
        [Validators.required, Validators.minLength(6), Validators.pattern('^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$')],
        [Validators.required],
      ],
    });
  }

  ngOnInit() {}

  submit(): void {
    if (this.userForm.invalid || this.role === undefined) {
      if (this.userForm.invalid) this.userForm.markAllAsTouched();
      if (this.role === undefined) this.isInvalid = true;
      return;
    }

    let user = new UserRequest(
      this.userForm.controls.login.value,
      this.userForm.controls.password.value,
      this.role ?? UserRole.EquipmentUser
    );

    this.addEvent.emit(user);
  }

  cancel() {
    this.addEvent.emit(null);
  }

  setUserRole(role: UserRole): void {
    this.role = role;
  }
}
