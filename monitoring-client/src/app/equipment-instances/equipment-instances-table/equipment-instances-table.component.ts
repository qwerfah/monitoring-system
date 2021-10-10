import { Component, OnInit } from '@angular/core';
import { v4 as uuid } from 'uuid';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { InstanceStatus } from 'src/app/models/instance-status';
import { EquipmentService } from 'src/app/services/equipment.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EquipmentInstanceRequest } from 'src/app/models/equipment-instance-request';

@Component({
  selector: 'app-equipment-instances-table',
  templateUrl: './equipment-instances-table.component.html',
  styleUrls: ['./equipment-instances-table.component.css'],
})
export class EquipmentInstancesTableComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;

  instances: EquipmentInstance[];

  constructor(private equipmentService: EquipmentService, private snackBar: MatSnackBar) {
    equipmentService.getInstances().subscribe(
      (instances) => {
        this.instances = instances;
        this.isLoading = false;
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
      }
    );
  }

  ngOnInit() {}

  openModal() {
    this.isAdding = true;
  }

  addInstance(instnace: [EquipmentInstanceRequest, string] | null) {
    this.isAdding = false;

    if (instnace === null) return;

    this.isLoading = true;

    this.equipmentService.addInstance(instnace[1], instnace[0]).subscribe(
      (instnace) => {
        this.snackBar.open('Успех: экземпляр добавлен', 'Ок');
        this.instances.push(instnace);
        this.isLoading = false;
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
          case 500: {
            this.snackBar.open('Ошибка: внутренняя ошибка сервера', 'Ок');
            break;
          }
          case 400: {
            this.snackBar.open('Ошибка: некорректные данные запроса', 'Ок');
          }
        }
        this.isLoading = false;
      }
    );
  }
}
