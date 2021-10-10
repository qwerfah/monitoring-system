import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EquipmentInstanceRequest } from 'src/app/models/equipment-instance-request';
import { EquipmentModel } from 'src/app/models/equipment-model';
import { EquipmentModelRequest } from 'src/app/models/equipment-model-request';
import { ParamRequest } from 'src/app/models/param-request';

@Component({
  selector: 'app-create-equipment-model',
  templateUrl: './create-equipment-model.component.html',
  styleUrls: ['./create-equipment-model.component.css'],
})
export class CreateEquipmentModelComponent implements OnInit {
  @Output() addEvent = new EventEmitter<EquipmentModelRequest | null>();

  modelForm: FormGroup;
  paramsForm: FormArray;

  constructor(private fb: FormBuilder) {
    this.modelForm = fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null, [Validators.minLength(0)]],
    });
    this.paramsForm = new FormArray([]);
  }

  ngOnInit() {}

  submit() {
    if (this.modelForm.invalid || this.paramsForm.invalid) {
      this.modelForm.markAllAsTouched();
      this.paramsForm.markAllAsTouched();
    } else {
      let params: ParamRequest[] = this.paramsForm.controls.map<ParamRequest>(
        (control) =>
          new ParamRequest(
            (control as FormGroup).controls.name.value,
            (control as FormGroup).controls.measurmentUnits.value
          )
      );

      let model: EquipmentModelRequest = new EquipmentModelRequest(
        this.modelForm.controls.name.value,
        this.modelForm.controls.description.value,
        params
      );

      this.addEvent.emit(model);
    }
  }

  cancel() {
    this.addEvent.emit(null);
  }

  addParamFormGroup() {
    this.paramsForm.push(
      this.fb.group({
        name: [null, [Validators.required, Validators.minLength(1)]],
        measurmentUnits: [null, [Validators.minLength(0)]],
      })
    );
  }

  removeParamFormGroup() {
    this.paramsForm.removeAt(this.paramsForm.length - 1);
  }
}
