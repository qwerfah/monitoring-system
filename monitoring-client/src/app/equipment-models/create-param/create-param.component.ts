import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ParamRequest } from 'src/app/models/param-request';

@Component({
  selector: 'app-create-param',
  templateUrl: './create-param.component.html',
  styleUrls: ['./create-param.component.css'],
})
export class CreateParamComponent implements OnInit {
  @Output() addEvent = new EventEmitter<ParamRequest | null>();

  paramForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.paramForm = fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      measurmentUnits: [null, [Validators.minLength(0)]],
    });
  }

  ngOnInit() {}

  submit() {
    if (this.paramForm.invalid) {
      this.paramForm.markAllAsTouched();
      return;
    }

    let instance = new ParamRequest(this.paramForm.controls.name.value, this.paramForm.controls.measurmentUnits.value);

    this.addEvent.emit(instance);
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
