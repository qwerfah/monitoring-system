import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EquipmentModel } from 'src/app/models/equipment-model';

@Component({
  selector: 'app-create-equipment-model',
  templateUrl: './create-equipment-model.component.html',
  styleUrls: ['./create-equipment-model.component.css'],
})
export class CreateEquipmentModelComponent implements OnInit {
  @Output() addEvent = new EventEmitter<EquipmentModel | null>();

  modelForm: FormGroup;
  paramsForm: FormArray;
  paramsCount: number = 3;

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
      this.addEvent.emit(new EquipmentModel('', 'name', 'desc'));

      this.paramsForm.controls.map((control) => {
        console.log((control as FormGroup).controls.name.value);
        console.log((control as FormGroup).controls.measurmentUnits.value);
      });

      console.log(`name: ${this.modelForm.controls.name.value}`);
      console.log(`description: ${this.modelForm.controls.description.value}`);
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
