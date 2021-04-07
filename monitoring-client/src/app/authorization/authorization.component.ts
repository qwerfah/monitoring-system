import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: "app-authorization",
  templateUrl: "./authorization.component.html",
  styleUrls: ["./authorization.component.css"],
})
export class AuthorizationComponent implements OnInit {
  credentials: FormGroup;

  constructor(private fb: FormBuilder) {
    this.credentials = fb.group({
      login: [null, [Validators.required]],
      password: [
        null,
        [
          Validators.required,
          Validators.minLength(6),
          Validators.pattern("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{6,}$"),
        ],
      ],
    });
  }

  ngOnInit(): void {}

  submit(): void {
    if (this.credentials.invalid) {
      this.credentials.markAllAsTouched();
    } else {
      console.log(`Email: ${this.credentials.controls.login.value}`);
      console.log(`Password: ${this.credentials.controls.password.value}`);
    }
  }
}
