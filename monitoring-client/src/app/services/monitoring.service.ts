import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Message } from '../models/message';

import { Monitor } from '../models/monitor';
import { MonitorRequest } from '../models/monitor-request';
import { Param } from '../models/param';
import { ParamValue } from '../models/param-value';
import { ServiceBase } from './service.base';

@Injectable({
  providedIn: 'root',
})
export class MonitoringService extends ServiceBase {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {
    super();
  }

  getMonitors(snackBar: MatSnackBar | null = null): Observable<Monitor[]> {
    return this.http
      .get<Monitor[]>(`${this.gatewayUri}/api/monitoring/monitors`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getMonitor(uid: string, snackBar: MatSnackBar | null = null): Observable<Monitor> {
    return this.http
      .get<Monitor>(`${this.gatewayUri}/api/monitoring/monitors/${uid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getInstancesMonitors(instanceUid: string, snackBar: MatSnackBar | null = null): Observable<Monitor[]> {
    return this.http
      .get<Monitor[]>(`${this.gatewayUri}/api/monitoring/instances/${instanceUid}/monitors`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getMonitorParams(uid: string, snackBar: MatSnackBar | null = null): Observable<Param[]> {
    return this.http
      .get<Param[]>(`${this.gatewayUri}/api/monitoring/monitors/${uid}/params`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getMonitorParamValues(uid: string, snackBar: MatSnackBar | null = null): Observable<ParamValue[]> {
    return this.http
      .get<ParamValue[]>(`${this.gatewayUri}/api/monitoring/monitors/${uid}/params/values`)
      .pipe(tap((values) => values.map((value) => (value.time = new Date(value.time)))))
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  addMonitor(instanceUid: string, monitor: MonitorRequest, snackBar: MatSnackBar | null = null): Observable<Monitor> {
    return this.http
      .post<Monitor>(`${this.gatewayUri}/api/monitoring/instances/${instanceUid}/monitors`, monitor)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  addMonitorParams(monitorUid: string, params: string[], snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .post<Message>(`${this.gatewayUri}/api/monitoring/monitors/${monitorUid}/params`, {
        params: params,
      })
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  removeMonitor(uid: string, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .delete<Message>(`${this.gatewayUri}/api/monitoring/monitors/${uid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  removeMonitorParam(monitorUid: string, paramUid: string, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .delete<Message>(`${this.gatewayUri}/api/monitoring/monitors/${monitorUid}/params/${paramUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }
}
