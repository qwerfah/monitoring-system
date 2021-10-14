import { Component, OnInit } from '@angular/core';
import { v4 as uuid } from 'uuid';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { InstanceStatus } from 'src/app/models/instance-status';
import { EquipmentService } from 'src/app/services/equipment.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EquipmentInstanceRequest } from 'src/app/models/equipment-instance-request';
import { UserRole } from 'src/app/models/user-role';
import { UserWithToken } from 'src/app/models/user';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-equipment-instances-table',
  templateUrl: './equipment-instances-table.component.html',
  styleUrls: ['./equipment-instances-table.component.css'],
})
export class EquipmentInstancesTableComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;

  instances: EquipmentInstance[];

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

  /** Check if current logged in user has sufficient rights to access element.
   * @param roles Array of sufficient user roles.
   * @returns True if current user role is sufficient, otherwise false.
   */
  isAllowed(roles: UserRole[]): boolean {
    return this.currentUser !== undefined && roles.indexOf(this.currentUser.role) !== -1;
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

  removeInstance(instanceUid: string): void {
    if (!confirm(`Удалить экземпляр ${this.instances.find((u) => u.uid === instanceUid)?.name}?`)) return;

    this.equipmentService.removeInstance(instanceUid).subscribe(
      (msg) => {
        this.instances.slice(
          this.instances.findIndex((i) => i.uid === instanceUid),
          1
        );
        this.snackBar.open('Успех: экземпляр удален', 'Ок');
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
            this.snackBar.open('Ошибка удаления: экземпляр не найден', 'Ок');
          }
        }
      }
    );
  }
}
