import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { Monitor } from '../models/monitor';
import { MonitorRequest } from '../models/monitor-request';
import { OperationRecord } from '../models/operation-record';
import { Param } from '../models/param';
import { ParamValue } from '../models/param-value';

@Injectable({
  providedIn: 'root',
})
export class ReportingService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  getOperationRecords(): Observable<OperationRecord[]> {
    return this.http.get<OperationRecord[]>(`${this.gatewayUri}/api/monitoring/monitors`);
  }
}
