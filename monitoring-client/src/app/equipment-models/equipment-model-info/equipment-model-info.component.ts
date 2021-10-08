import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { zip } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { EquipmentService } from 'src/app/services/equipment.service';

import { EquipmentModel } from 'src/app/models/equipment-model';
import { Param } from 'src/app/models/param';
import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { InstanceStatus } from 'src/app/models/instance-status';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { mergeAll } from 'rxjs/operators';

@Component({
  selector: 'app-equipment-model-info',
  templateUrl: './equipment-model-info.component.html',
  styleUrls: ['./equipment-model-info.component.css'],
})
export class EquipmentModelInfoComponent implements OnInit {
  isLoading: boolean = true;
  modelUid: string;
  model: EquipmentModel;
  params: Param[];
  instances: EquipmentInstance[];

  constructor(
    private route: ActivatedRoute,
    private equipmentService: EquipmentService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.modelUid = this.route.snapshot.params['uid'];

    zip(
      this.equipmentService.getModel(this.modelUid),
      this.equipmentService.getModelParams(this.modelUid),
      this.equipmentService.getModelInstances(this.modelUid)
    ).subscribe(
      (result) => {
        this.model = result[0];
        this.params = result[1];
        this.instances = result[2];
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис оборудования недоступен', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: данные не найдены', 'Ок');
          }
        }
        this.isLoading = false;
      },
      () => {
        this.isLoading = false;
      }
    );
  }
}
