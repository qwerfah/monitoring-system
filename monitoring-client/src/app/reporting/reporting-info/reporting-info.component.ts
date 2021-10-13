import { Component, OnInit } from '@angular/core';
import { v4 as uuid } from 'uuid';

import { OperationRecord } from 'src/app/models/operation-record';
import { ReportingService } from 'src/app/services/reporting.service';
import { HttpMethod } from 'src/app/models/http-method';
import { ServiceStat } from 'src/app/models/service-stat';
import { ModelStat } from 'src/app/models/model-stat';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-reporting-info',
  templateUrl: './reporting-info.component.html',
  styleUrls: ['./reporting-info.component.css'],
})
export class ReportingInfoComponent implements OnInit {
  operationRecords: OperationRecord[];
  serviceStats: ServiceStat[];
  modelStats: ModelStat[];

  isLoading: boolean = true;

  constructor(private reportingService: ReportingService, private snackBar: MatSnackBar) {
    this.operationRecords = [];
    this.serviceStats = [];
    this.modelStats = [];
  }

  ngOnInit() {
    this.getServiceStats();
    this.getModelStats();
  }

  private getModelStats(): void {
    this.reportingService.getModelStats().subscribe(
      (stats) => {
        this.isLoading = false;
        this.modelStats = stats;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок', { duration: 5000 });
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис недоступен', 'Ок', { duration: 5000 });
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: данные не найдены', 'Ок', { duration: 5000 });
          }
        }
        this.isLoading = false;
      }
    );
  }

  private getServiceStats(): void {
    this.reportingService.getServiceStats().subscribe(
      (stats) => {
        this.serviceStats = stats;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок', { duration: 5000 });
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис недоступен', 'Ок', { duration: 5000 });
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: данные не найдены', 'Ок', { duration: 5000 });
          }
        }
        this.isLoading = false;
      }
    );
  }
}
