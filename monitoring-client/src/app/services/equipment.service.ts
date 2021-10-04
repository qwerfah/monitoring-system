import { Injectable, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Param } from '../models/param';
import { ParamRequest } from '../models/param-request';
import { Message } from '../models/message';
import { EquipmentInstanceRequest } from '../models/equipment-instance-request';
import { EquipmentModel } from '../models/equipment-model';
import { EquipmentModelRequest } from '../models/equipment-model-request';
import { EquipmentInstance } from '../models/equipment-instance';

@Injectable({
  providedIn: 'root',
})
export class EquipmentService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  getModels(): Observable<EquipmentModel[]> {
    return this.http.get<EquipmentModel[]>(`${this.gatewayUri}api/equipment/models`);
  }

  getModel(modelUid: string): Observable<EquipmentModel> {
    return this.http.get<EquipmentModel>(`${this.gatewayUri}api/equipment/models/${modelUid}`);
  }

  addModel(request: EquipmentModelRequest): Observable<EquipmentModel> {
    return this.http.post<EquipmentModel>(`${this.gatewayUri}api/equipment/models`, request);
  }

  updateModel(modelUid: string, request: EquipmentModelRequest): Observable<Message> {
    return this.http.patch<EquipmentModel>(`${this.gatewayUri}api/equipment/models/${modelUid}`, request);
  }

  removeModel(modelUid: string): Observable<Message> {
    return this.http.delete<Message>(`${this.gatewayUri}api/equipment/models/${modelUid}`);
  }

  getInstances(): Observable<EquipmentInstance[]> {
    return this.http.get<EquipmentInstance[]>(`${this.gatewayUri}api/equipment/instances`);
  }

  getModelInstances(modelUid: string): Observable<EquipmentInstance[]> {
    return this.http.get<EquipmentInstance[]>(`${this.gatewayUri}api/equipment/models/${modelUid}/instances`);
  }

  getInstance(instanceUid: string): Observable<EquipmentInstance> {
    return this.http.get<EquipmentInstance>(`${this.gatewayUri}api/equipment/instances/${instanceUid}`);
  }

  addInstance(modelUid: string, request: EquipmentInstanceRequest): Observable<EquipmentInstance> {
    return this.http.post<EquipmentInstance>(`${this.gatewayUri}api/equipment/models/${modelUid}/instances`, request);
  }

  updateInstance(instanceUid: string, request: EquipmentInstanceRequest): Observable<Message> {
    return this.http.patch<Message>(`${this.gatewayUri}api/equipment/instances/${instanceUid}`, request);
  }

  removeInstance(instanceUid: string): Observable<Message> {
    return this.http.delete<Message>(`${this.gatewayUri}api/equipment/instances/${instanceUid}`);
  }

  getModelParams(modelUid: string): Observable<Param[]> {
    return this.http.get<Param[]>(`${this.gatewayUri}api/equipment/models/${modelUid}/params`);
  }

  getInstanceParams(instanaceUid: string): Observable<Param[]> {
    return this.http.get<Param[]>(`${this.gatewayUri}api/equipment/instances/${instanaceUid}/params`);
  }

  getParam(paramUid: string): Observable<Param> {
    return this.http.get<Param>(`${this.gatewayUri}api/equipment/params/${paramUid}`);
  }

  addParam(modelUid: string, request: ParamRequest): Observable<Param> {
    return this.http.post<Param>(`${this.gatewayUri}api/equipment/models/${modelUid}/params`, request);
  }

  updateParam(paramUid: string, request: ParamRequest): Observable<Message> {
    return this.http.patch<Message>(`${this.gatewayUri}api/equipment/params/${paramUid}`, request);
  }

  removeParam(paramUid: string): Observable<Message> {
    return this.http.delete<Message>(`${this.gatewayUri}api/equipment/params/${paramUid}`);
  }
}
