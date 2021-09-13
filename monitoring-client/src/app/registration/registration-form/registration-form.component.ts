import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.css'],
})
export class RegistrationFormComponent implements OnInit {
  userInfo: FormGroup;

  constructor(private fb: FormBuilder) {
    this.userInfo = fb.group({
      name: [null, [Validators.required]],
      login: [null, [Validators.required]],
      password: [
        null,
        [
          Validators.required,
          Validators.minLength(6),
          Validators.pattern('^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$'),
        ],
      ],
      confirmation: [
        null,
        [
          Validators.required,
          Validators.minLength(6),
          Validators.pattern('^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$'),
        ],
      ],
    });
  }

  ngOnInit(): void {}

  submit(): void {
    if (this.userInfo.invalid) {
      this.userInfo.markAllAsTouched();
    } else {
      console.log(`Email: ${this.userInfo.controls.name.value}`);
      console.log(`Email: ${this.userInfo.controls.login.value}`);
      console.log(`Password: ${this.userInfo.controls.password.value}`);
      console.log(`Password: ${this.userInfo.controls.confirmation.value}`);
    }
  }
}
