import { Component, ElementRef, EventEmitter, OnInit, Output, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatMenuTrigger } from '@angular/material/menu';
import { Observable, of } from 'rxjs';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { EquipmentModel } from 'src/app/models/equipment-model';
import { InstanceStatus } from 'src/app/models/instance-status';
import { MonitorRequest } from 'src/app/models/monitor-request';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-create-monitor',
  templateUrl: './create-monitor.component.html',
  styleUrls: ['./create-monitor.component.css'],
})
export class CreateMonitorComponent implements OnInit {
  @Output() addEvent = new EventEmitter<MonitorRequest | null>();

  monitorForm: FormGroup;

  selectedModel: EquipmentModel;
  selectedInstance: EquipmentInstance;

  models$: Observable<EquipmentModel[]>;
  instances$: Observable<EquipmentInstance[]>;

  @ViewChildren(MatMenuTrigger) trigger: QueryList<MatMenuTrigger>;

  constructor(private fb: FormBuilder, private equipmentService: EquipmentService) {
    this.monitorForm = fb.group({
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

    this.selectedModel = new EquipmentModel('', '', '');
    this.selectedInstance = new EquipmentInstance('', '', '', '', '', InstanceStatus.Active);
  }

  ngOnInit() {}

  openModelsMenu() {
    this.trigger.toArray()[0].openMenu();
  }

  openInstancesMenu() {
    this.trigger.toArray()[1].openMenu();
  }

  setModel(model: EquipmentModel): void {
    this.selectedModel = model;
    this.instances$ = of([
      new EquipmentInstance('', '', 'instance_1', 'model_1', '', InstanceStatus.Active),
      new EquipmentInstance('', '', 'instance_2', 'model_2', '', InstanceStatus.Active),
      new EquipmentInstance('', '', 'instance_3', 'model_3', '', InstanceStatus.Active),
      new EquipmentInstance('', '', 'instance_4', 'model_4', '', InstanceStatus.Active),
      new EquipmentInstance('', '', 'instance_5', 'model_5', '', InstanceStatus.Active),
    ]);
  }

  setInstance(instance: EquipmentInstance): void {
    this.selectedInstance = instance;
  }

  submit() {
    if (this.monitorForm.invalid) {
      this.monitorForm.markAllAsTouched();
    } else {
      this.addEvent.emit(new MonitorRequest('name', 'model'));

      console.log(`name: ${this.monitorForm.controls.name.value}`);
      console.log(`description: ${this.monitorForm.controls.description.value}`);
    }
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
