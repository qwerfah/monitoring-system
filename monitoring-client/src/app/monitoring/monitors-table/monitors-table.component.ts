import { Component, Directive, ElementRef, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { Monitor } from 'src/app/models/monitor';
import { MonitorRequest } from 'src/app/models/monitor-request';
import { MonitoringService } from 'src/app/services/monitoring.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-monitors-table',
  templateUrl: './monitors-table.component.html',
  styleUrls: ['./monitors-table.component.css'],
})
export class MonitorsTableComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;

  monitors: Monitor[];

  constructor(private monitoringService: MonitoringService, private snackBar: MatSnackBar) {
    this.monitoringService.getMonitors().subscribe(
      (monitors) => {
        this.monitors = monitors;
        this.isLoading = false;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис мониторинга недоступен', 'Ок');
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

  ngOnInit() {}

  openModal() {
    this.isAdding = true;
  }

  addMonitor(monitor: MonitorRequest | null) {
    this.isAdding = false;
  }
}
