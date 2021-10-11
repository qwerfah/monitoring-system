import { Component, OnInit } from '@angular/core';

import { EquipmentModel } from '../../models/equipment-model';

import { EquipmentService } from 'src/app/services/equipment.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EquipmentModelRequest } from 'src/app/models/equipment-model-request';

@Component({
  selector: 'app-equipment-models',
  templateUrl: './equipment-models-table.component.html',
  styleUrls: ['./equipment-models-table.component.css'],
})
export class EquipmentModelsTableComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;
  models: EquipmentModel[];

  constructor(private equipmentService: EquipmentService, private snackBar: MatSnackBar) {
    equipmentService.getModels().subscribe(
      (models) => {
        this.models = models;
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

  addModel(model: EquipmentModelRequest | null) {
    this.isAdding = false;

    console.log(model);

    if (model === null) return;

    this.isLoading = true;

    this.equipmentService.addModel(model).subscribe(
      (model) => {
        this.snackBar.open('Успех: модель добавлена', 'Ок');
        this.models.push(model);
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
