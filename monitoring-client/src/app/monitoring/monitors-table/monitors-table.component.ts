import { Component, Directive, ElementRef, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { Monitor } from 'src/app/models/monitor';
import { MonitorRequest } from 'src/app/models/monitor-request';
import { MonitoringService } from 'src/app/services/monitoring.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserRole } from 'src/app/models/user-role';
import { UserWithToken } from 'src/app/models/user';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-monitors-table',
  templateUrl: './monitors-table.component.html',
  styleUrls: ['./monitors-table.component.css'],
})
export class MonitorsTableComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;

  monitors: Monitor[];

  UserRole = UserRole;

  currentUser: UserWithToken | undefined = undefined;

  constructor(
    private sessionService: SessionService,
    private monitoringService: MonitoringService,
    private snackBar: MatSnackBar
  ) {
    sessionService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });

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
      }
    );
  }

  ngOnInit() {}

  openModal() {
    this.isAdding = true;
  }

  addMonitor(monitor: [string, MonitorRequest] | null) {
    this.isAdding = false;

    if (monitor === null) return;

    this.isLoading = true;
    this.monitoringService.addMonitor(monitor[0], monitor[1]).subscribe(
      (monitor) => {
        this.snackBar.open('Успех: экран мониторинга создан', 'Ок');
        this.isLoading = false;
        this.monitors.push(monitor);
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
      }
    );
  }

  /** Check if current logged in user has sufficient rights to access element.
   * @param roles Array of sufficient user roles.
   * @returns True if current user role is sufficient, otherwise false.
   */
  isAllowed(roles: UserRole[]): boolean {
    return this.currentUser !== undefined && roles.indexOf(this.currentUser.role) !== -1;
  }

  removeMonitor(monitorUid: string): void {
    if (!confirm(`Удалить монитор ${this.monitors.find((u) => u.uid === monitorUid)?.name}?`)) return;

    this.monitoringService.removeMonitor(monitorUid).subscribe(
      (msg) => {
        this.monitors.slice(
          this.monitors.findIndex((m) => m.uid === monitorUid),
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
