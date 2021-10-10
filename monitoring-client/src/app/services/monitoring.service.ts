import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Monitor } from '../models/monitor';

@Injectable({
  providedIn: 'root',
})
export class MonitoringService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  getMonitors(): Observable<Monitor[]> {
    return this.http.get<Monitor[]>(`${this.gatewayUri}/api/monitoring/monitors`);
  }

  getInstancesMonitors(instanceUid: string): Observable<Monitor[]> {
    return this.http.get<Monitor[]>(`${this.gatewayUri}/api/monitoring/instances/${instanceUid}/monitors`);
  }
}
