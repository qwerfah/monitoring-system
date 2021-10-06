import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { EquipmentModel } from 'src/app/models/equipment-model';
import { InstanceStatus } from 'src/app/models/instance-status';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-create-equipment-instance',
  templateUrl: './create-equipment-instance.component.html',
  styleUrls: ['./create-equipment-instance.component.css'],
})
export class CreateEquipmentInstanceComponent implements OnInit {
  @Output() addEvent = new EventEmitter<EquipmentInstance | null>();

  InstanceStatus = InstanceStatus;

  modelForm: FormGroup;
  status: InstanceStatus;

  models$: Observable<EquipmentModel[]>;

  constructor(private fb: FormBuilder, private equipmentService: EquipmentService) {
    this.modelForm = fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null, [Validators.minLength(0)]],
    });

    this.models$ = equipmentService.getModels();
    this.status = InstanceStatus.Active;
  }

  ngOnInit() {}

  setStatus(status: InstanceStatus) {
    this.status = status;
  }

  submit() {
    if (this.modelForm.invalid) {
      this.modelForm.markAllAsTouched();
    } else {
      this.addEvent.emit(new EquipmentInstance('', '', 'name', 'desc', InstanceStatus.Active));

      console.log(`name: ${this.modelForm.controls.name.value}`);
      console.log(`description: ${this.modelForm.controls.description.value}`);
    }
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
