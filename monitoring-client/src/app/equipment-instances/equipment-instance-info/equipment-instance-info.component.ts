import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of, zip } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { EquipmentService } from 'src/app/services/equipment.service';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { Monitor } from 'src/app/models/monitor';
import { Param } from 'src/app/models/param';
import { InstanceStatus } from 'src/app/models/instance-status';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MonitoringService } from 'src/app/services/monitoring.service';
import { UserWithToken } from 'src/app/models/user';
import { UserRole } from 'src/app/models/user-role';
import { SessionService } from 'src/app/services/session.service';
import { EquipmentInstanceRequest } from 'src/app/models/equipment-instance-request';
import { UserDependentComponent } from 'src/app/helpers/user-dependent.component';

@Component({
  selector: 'app-equipment-instance-info',
  templateUrl: './equipment-instance-info.component.html',
  styleUrls: ['./equipment-instance-info.component.css'],
})
export class EquipmentInstanceInfoComponent extends UserDependentComponent implements OnInit {
  isLoading: boolean = true;
  isEditing: boolean = false;

  instanceUid: string;
  instance: EquipmentInstance;
  params: Param[];
  monitors: Monitor[];

  constructor(
    private route: ActivatedRoute,
    sessionService: SessionService,
    private equipmentService: EquipmentService,
    private monitoringService: MonitoringService,
    private snackBar: MatSnackBar
  ) {
    super(sessionService);
  }

  ngOnInit() {
    this.instanceUid = this.route.snapshot.params['uid'];

    this.equipmentService.getInstance(this.instanceUid).subscribe(
      (instance) => {
        this.instance = instance;
        this.isLoading = false;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок', { duration: 5000 });
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис оборудования недоступен', 'Ок', { duration: 5000 });
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: данные не найдены', 'Ок', { duration: 5000 });
          }
        }
        this.isLoading = false;
      }
    );

    this.equipmentService.getInstanceParams(this.instanceUid).subscribe(
      (params) => {
        this.params = params;
        this.isLoading = false;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок', { duration: 5000 });
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис оборудования недоступен', 'Ок', { duration: 5000 });
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: данные не найдены', 'Ок', { duration: 5000 });
          }
        }
        this.isLoading = false;
      }
    );

    this.monitoringService.getInstancesMonitors(this.instanceUid, this.snackBar).subscribe(
      (monitors) => {
        this.monitors = monitors;
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }

  openModal() {
    this.isEditing = true;
  }

  updateInstance(request: EquipmentInstanceRequest | null) {
    this.isEditing = false;

    if (request === null) return;

    this.isLoading = true;

    this.equipmentService.updateInstance(this.instanceUid, request, this.snackBar).subscribe(
      () => {
        this.instance.name = request.name;
        this.instance.description = request.description;
        this.instance.status = request.status;
        this.snackBar.open('Успех: данные обновлены', 'Ок', { duration: 5000 });
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }
}
