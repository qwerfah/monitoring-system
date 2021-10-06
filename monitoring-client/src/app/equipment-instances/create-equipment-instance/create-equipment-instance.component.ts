import { Component, EventEmitter, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatMenuTrigger } from '@angular/material/menu';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';

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

  instanceForm: FormGroup;

  status: InstanceStatus;
  selectedModel: EquipmentModel;

  models$: Observable<EquipmentModel[]>;

  @ViewChildren(MatMenuTrigger) trigger: QueryList<MatMenuTrigger>;

  constructor(private fb: FormBuilder, private equipmentService: EquipmentService) {
    this.instanceForm = fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null, [Validators.minLength(0)]],
    });

    this.models$ = of([
      new EquipmentModel('', 'model_1', 'description_1'),
      new EquipmentModel('', 'model_2', 'description_2'),
      new EquipmentModel('', 'model_3', 'description_3'),
      new EquipmentModel('', 'model_4', 'description_4'),
      new EquipmentModel('', 'model_5', 'description_5'),
      new EquipmentModel('', 'model_1', 'description_1'),
      new EquipmentModel('', 'model_2', 'description_2'),
      new EquipmentModel('', 'model_3', 'description_3'),
      new EquipmentModel('', 'model_4', 'description_4'),
      new EquipmentModel('', 'model_5', 'description_5'),
      new EquipmentModel('', 'model_1', 'description_1'),
      new EquipmentModel('', 'model_2', 'description_2'),
      new EquipmentModel('', 'model_3', 'description_3'),
      new EquipmentModel('', 'model_4', 'description_4'),
      new EquipmentModel('', 'model_5', 'description_5'),
    ]);

    this.status = InstanceStatus.Active;
    this.selectedModel = new EquipmentModel('', '', '');
  }

  ngOnInit() {}

  openStatusMenu() {
    this.trigger.toArray()[0].openMenu();
  }

  openModelsMenu() {
    this.trigger.toArray()[1].openMenu();
  }

  setStatus(status: InstanceStatus): void {
    this.status = status;
  }

  setModel(model: EquipmentModel): void {
    this.selectedModel = model;
  }

  submit() {
    if (this.instanceForm.invalid) {
      this.instanceForm.markAllAsTouched();
    } else {
      this.addEvent.emit(new EquipmentInstance('', '', 'name', 'desc', InstanceStatus.Active));

      console.log(`name: ${this.instanceForm.controls.name.value}`);
      console.log(`description: ${this.instanceForm.controls.description.value}`);
    }
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
