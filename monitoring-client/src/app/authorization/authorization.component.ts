import { Component, OnInit } from "@angular/core";
import { AuthorizationModule } from "./authorization.module";
import {
  FormBuilder,
  FormGroup,
  FormControl,
  Validators,
} from "@angular/forms";

@Component({
  selector: "app-authorization",
  templateUrl: "./authorization.component.html",
  styleUrls: ["./authorization.component.css"],
})
export class AuthorizationComponent implements OnInit {
  credentials: FormGroup;

  constructor(private fb: FormBuilder) {
    this.credentials = fb.group({
      email: [null, [Validators.required, Validators.email]],
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

  login(): void {
    console.log(`Email: ${this.credentials.controls.email}`);
    console.log(`Password: ${this.credentials.controls.password}`);
  }

  submit(): void {
    console.log(`Email: ${this.credentials.controls.email.value}`);
    console.log(`Password: ${this.credentials.controls.password.value}`);
  }
}
