import { Component, OnInit } from '@angular/core';
import { v4 as uuid } from 'uuid';

import { OperationRecord } from 'src/app/models/operation-record';
import { ReportingService } from 'src/app/services/reporting.service';
import { HttpMethod } from 'src/app/models/http-method';
import { ServiceStat } from 'src/app/models/service-stat';
import { ModelStat } from 'src/app/models/model-stat';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { UserDependentComponent } from 'src/app/helpers/user-dependent.component';

@Component({
  selector: 'app-reporting-info',
  templateUrl: './reporting-info.component.html',
  styleUrls: ['./reporting-info.component.css'],
})
export class ReportingInfoComponent extends UserDependentComponent implements OnInit {
  operationRecords: OperationRecord[];
  serviceStats: ServiceStat[];
  modelStats: ModelStat[];

  isLoading: boolean = true;

  constructor(
    sessionService: SessionService,
    private reportingService: ReportingService,
    private snackBar: MatSnackBar
  ) {
    super(sessionService);

    this.operationRecords = [];
    this.serviceStats = [];
    this.modelStats = [];
  }

  ngOnInit() {
    this.getServiceStats();
    this.getModelStats();
  }

  private getModelStats(): void {
    this.reportingService.getModelStats(this.snackBar).subscribe(
      (stats) => {
        this.isLoading = false;
        this.modelStats = stats;
      },
      () => (this.isLoading = false)
    );
  }

  private getServiceStats(): void {
    this.reportingService.getServiceStats(this.snackBar).subscribe(
      (stats) => {
        this.serviceStats = stats;
      },
      () => (this.isLoading = false)
    );
  }
}
