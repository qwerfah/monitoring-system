import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ModelStat } from '../models/model-stat';
import { ServiceStat } from '../models/service-stat';
import { ServiceBase } from './service.base';

@Injectable({
  providedIn: 'root',
})
export class ReportingService extends ServiceBase {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {
    super();
  }

  getServiceStats(snackBar: MatSnackBar | null = null): Observable<ServiceStat[]> {
    return this.http
      .get<ServiceStat[]>(`${this.gatewayUri}/api/reporting/services/stats`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getModelStats(snackBar: MatSnackBar | null = null): Observable<ModelStat[]> {
    return this.http
      .get<ModelStat[]>(`${this.gatewayUri}/api/reporting/models/stats`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }
}
