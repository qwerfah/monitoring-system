import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { EquipmentInstanceRequest } from 'src/app/models/equipment-instance-request';
import { EquipmentModel } from 'src/app/models/equipment-model';
import { InstanceStatus } from 'src/app/models/instance-status';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-create-equipment-instance',
  templateUrl: './create-equipment-instance.component.html',
  styleUrls: ['./create-equipment-instance.component.css'],
})
export class CreateEquipmentInstanceComponent implements OnInit {
  @Output() addEvent = new EventEmitter<[EquipmentInstanceRequest, string] | null>();

  InstanceStatus = InstanceStatus;

  isLoading: boolean = true;

  instanceForm: FormGroup;

  status: InstanceStatus;
  selectedModel: EquipmentModel;

  models: EquipmentModel[];

  @ViewChildren(MatMenuTrigger) trigger: QueryList<MatMenuTrigger>;

  constructor(private fb: FormBuilder, private equipmentService: EquipmentService, private snackBar: MatSnackBar) {
    this.instanceForm = fb.group({
      name: [null, [Validators.required, Validators.minLength(3)]],
      description: [null, [Validators.minLength(0)]],
    });

    this.equipmentService.getModels(this.snackBar).subscribe(
      (models) => {
        this.models = models;
        this.isLoading = false;
      },
      () => {
        this.isLoading = false;
        this.addEvent.emit(null);
      }
    );

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
      let instance = new EquipmentInstanceRequest(
        this.instanceForm.controls.name.value,
        this.instanceForm.controls.description.value,
        this.status
      );

      this.addEvent.emit([instance, this.selectedModel.uid]);
    }
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
