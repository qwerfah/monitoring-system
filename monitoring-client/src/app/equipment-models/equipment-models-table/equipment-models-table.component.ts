import { Component, OnInit } from '@angular/core';

import { EquipmentModel } from '../../models/equipment-model';

import { EquipmentService } from 'src/app/services/equipment.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EquipmentModelRequest } from 'src/app/models/equipment-model-request';
import { UserWithToken } from 'src/app/models/user';
import { SessionService } from 'src/app/services/session.service';
import { UserRole } from 'src/app/models/user-role';

@Component({
  selector: 'app-equipment-models',
  templateUrl: './equipment-models-table.component.html',
  styleUrls: ['./equipment-models-table.component.css'],
})
export class EquipmentModelsTableComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;
  models: EquipmentModel[];

  UserRole = UserRole;

  currentUser: UserWithToken | undefined = undefined;

  constructor(
    private sessionService: SessionService,
    private equipmentService: EquipmentService,
    private snackBar: MatSnackBar
  ) {
    sessionService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });

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

  /** Check if current logged in user has sufficient rights to access element.
   * @param roles Array of sufficient user roles.
   * @returns True if current user role is sufficient, otherwise false.
   */
  isAllowed(roles: UserRole[]): boolean {
    return this.currentUser !== undefined && roles.indexOf(this.currentUser.role) !== -1;
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
            this.snackBar.open('Ошибка добавления: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка добавления: сервис оборудования недоступен', 'Ок');
            break;
          }
          case 500: {
            this.snackBar.open('Ошибка добавления: внутренняя ошибка сервера', 'Ок');
            break;
          }
          case 400: {
            this.snackBar.open('Ошибка добавления: некорректные данные запроса', 'Ок');
          }
        }
        this.isLoading = false;
      }
    );
  }

  removeModel(modelUid: string): void {
    if (!confirm(`Удалить модель ${this.models.find((u) => u.uid === modelUid)?.name}?`)) return;

    this.equipmentService.removeModel(modelUid).subscribe(
      (msg) => {
        this.models.splice(
          this.models.findIndex((m) => m.uid === modelUid),
          1
        );
        this.snackBar.open('Успех: модель удалена', 'Ок');
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка удаления: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка удаления: сервис оборудования недоступен', 'Ок');
            break;
          }
          case 500: {
            this.snackBar.open('Ошибка удаления: внутренняя ошибка сервера', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка удаления: модель не найдена', 'Ок');
            break;
          }
          case 422: {
            this.snackBar.open('Ошибка удаления: не удалось удалить все связные сущности', 'Ок');
          }
        }
      }
    );
  }
}
