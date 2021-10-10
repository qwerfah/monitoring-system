import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { Monitor } from '../models/monitor';
import { MonitorRequest } from '../models/monitor-request';
import { Param } from '../models/param';
import { ParamValue } from '../models/param-value';

@Injectable({
  providedIn: 'root',
})
export class MonitoringService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  getMonitors(): Observable<Monitor[]> {
    return this.http.get<Monitor[]>(`${this.gatewayUri}/api/monitoring/monitors`);
  }

  getMonitor(uid: string): Observable<Monitor> {
    return this.http.get<Monitor>(`${this.gatewayUri}/api/monitoring/monitors/${uid}`);
  }

  getInstancesMonitors(instanceUid: string): Observable<Monitor[]> {
    return this.http.get<Monitor[]>(`${this.gatewayUri}/api/monitoring/instances/${instanceUid}/monitors`);
  }

  getMonitorParams(uid: string): Observable<Param[]> {
    return this.http.get<Param[]>(`${this.gatewayUri}/api/monitoring/monitors/${uid}/params`);
  }

  getMonitorParamValues(uid: string): Observable<ParamValue[]> {
    return this.http
      .get<ParamValue[]>(`${this.gatewayUri}/api/monitoring/monitors/${uid}/params/values`)
      .pipe(tap((values) => values.map((value) => (value.time = new Date(value.time)))));
  }

  addMonitor(instanceUid: string, monitor: MonitorRequest): Observable<Monitor> {
    return this.http.post<Monitor>(`${this.gatewayUri}/api/monitoring/instances/${instanceUid}/monitors`, monitor);
  }
}
