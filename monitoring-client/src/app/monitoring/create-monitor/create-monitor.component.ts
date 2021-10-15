import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, EventEmitter, OnInit, Output, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, of } from 'rxjs';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { EquipmentModel } from 'src/app/models/equipment-model';
import { InstanceStatus } from 'src/app/models/instance-status';
import { MonitorRequest } from 'src/app/models/monitor-request';
import { Param } from 'src/app/models/param';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-create-monitor',
  templateUrl: './create-monitor.component.html',
  styleUrls: ['./create-monitor.component.css'],
})
export class CreateMonitorComponent implements OnInit {
  @Output() addEvent = new EventEmitter<[string, MonitorRequest] | null>();
  @ViewChildren(MatMenuTrigger) trigger: QueryList<MatMenuTrigger>;

  Param = Param;

  isLoading: boolean = true;
  isInvalid: boolean = false;

  monitorForm: FormGroup;
  paramsControl = new FormControl();

  selectedModel: EquipmentModel | undefined = undefined;
  selectedInstance: EquipmentInstance | undefined = undefined;

  models: EquipmentModel[];
  instances: EquipmentInstance[];
  params: Param[];

  constructor(private fb: FormBuilder, private equipmentService: EquipmentService, private snackBar: MatSnackBar) {
    this.monitorForm = fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null, [Validators.minLength(0)]],
    });
  }

  ngOnInit() {
    this.equipmentService.getModels(this.snackBar).subscribe(
      (models) => {
        this.models = models;
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }

  openModelsMenu() {
    this.trigger.toArray()[0].openMenu();
  }

  openInstancesMenu() {
    this.trigger.toArray()[1].openMenu();
  }

  setModel(model: EquipmentModel): void {
    this.selectedModel = model;
    this.equipmentService.getActiveModelInstances(model.uid, this.snackBar).subscribe((instances) => {
      this.instances = instances;
    });
  }

  setInstance(instance: EquipmentInstance): void {
    this.selectedInstance = instance;
    this.equipmentService.getInstanceParams(instance.uid, this.snackBar).subscribe((params) => {
      this.params = params;
    });
  }

  submit() {
    if (this.monitorForm.invalid || !this.selectedModel || !this.selectedInstance) {
      this.monitorForm.markAllAsTouched();
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
      let params = this.paramsControl.value ? (this.paramsControl.value as Param[]).map((p: Param) => p.uid) : [];
      let monitor = new MonitorRequest(
        this.monitorForm.controls.name.value,
        this.monitorForm.controls.description.value,
        params
      );

      this.addEvent.emit([this.selectedInstance.uid, monitor]);
    }
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
