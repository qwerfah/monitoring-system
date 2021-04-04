import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: "app-registration",
  templateUrl: "./registration.component.html",
  styleUrls: ["./registration.component.css"],
})
export class RegistrationComponent implements OnInit {
  userInfo: FormGroup;

  constructor(private fb: FormBuilder) {
    this.userInfo = fb.group({
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
}
