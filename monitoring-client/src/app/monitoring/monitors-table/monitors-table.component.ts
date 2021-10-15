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
import { UserDependentComponent } from 'src/app/helpers/user-dependent.component';

@Component({
  selector: 'app-monitors-table',
  templateUrl: './monitors-table.component.html',
  styleUrls: ['./monitors-table.component.css'],
})
export class MonitorsTableComponent extends UserDependentComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;

  monitors: Monitor[];

  constructor(
    sessionService: SessionService,
    private monitoringService: MonitoringService,
    private snackBar: MatSnackBar
  ) {
    super(sessionService);
  }

  ngOnInit() {
    this.monitoringService.getMonitors(this.snackBar).subscribe(
      (monitors) => {
        this.monitors = monitors;
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }

  openModal() {
    this.isAdding = true;
  }

  addMonitor(monitor: [string, MonitorRequest] | null) {
    this.isAdding = false;
    if (monitor === null) return;
    this.isLoading = true;

    this.monitoringService.addMonitor(monitor[0], monitor[1], this.snackBar).subscribe(
      (monitor) => {
        this.snackBar.open('Успех: экран мониторинга создан', 'Ок', { duration: 5000 });
        this.isLoading = false;
        this.monitors.push(monitor);
      },
      () => (this.isLoading = false)
    );
  }

  removeMonitor(monitorUid: string): void {
    if (!confirm(`Удалить монитор ${this.monitors.find((u) => u.uid === monitorUid)?.name}?`)) return;

    this.monitoringService.removeMonitor(monitorUid, this.snackBar).subscribe((msg) => {
      this.monitors.splice(
        this.monitors.findIndex((m) => m.uid === monitorUid),
        1
      );
      this.snackBar.open('Успех: экран мониторинга удален', 'Ок', { duration: 5000 });
    });
  }
}
