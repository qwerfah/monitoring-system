import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ModelStat } from '../models/model.stat';
import { ServiceStat } from '../models/service.stat';

@Injectable({
  providedIn: 'root',
})
export class ReportingService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  getServiceStats(): Observable<ServiceStat[]> {
    return this.http.get<ServiceStat[]>(`${this.gatewayUri}/api/reporting/services/stats`);
  }

  getModelStats(): Observable<ModelStat[]> {
    return this.http.get<ModelStat[]>(`${this.gatewayUri}/api/reporting/models/stats`);
  }
}
