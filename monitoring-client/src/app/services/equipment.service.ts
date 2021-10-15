import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';

import { Param } from '../models/param';
import { ParamRequest } from '../models/param-request';
import { Message } from '../models/message';
import { EquipmentInstanceRequest } from '../models/equipment-instance-request';
import { EquipmentModel } from '../models/equipment-model';
import { EquipmentModelRequest } from '../models/equipment-model-request';
import { EquipmentInstance } from '../models/equipment-instance';
import { catchError } from 'rxjs/operators';
import { ServiceBase } from './service.base';

@Injectable({
  providedIn: 'root',
})
export class EquipmentService extends ServiceBase {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {
    super();
  }

  getModels(snackBar: MatSnackBar | null = null): Observable<EquipmentModel[]> {
    return this.http
      .get<EquipmentModel[]>(`${this.gatewayUri}/api/equipment/models`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getModel(modelUid: string, snackBar: MatSnackBar | null = null): Observable<EquipmentModel> {
    return this.http
      .get<EquipmentModel>(`${this.gatewayUri}/api/equipment/models/${modelUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  addModel(request: EquipmentModelRequest, snackBar: MatSnackBar | null = null): Observable<EquipmentModel> {
    return this.http
      .post<EquipmentModel>(`${this.gatewayUri}/api/equipment/models`, request)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  updateModel(
    modelUid: string,
    request: EquipmentModelRequest,
    snackBar: MatSnackBar | null = null
  ): Observable<Message> {
    return this.http
      .patch<EquipmentModel>(`${this.gatewayUri}/api/equipment/models/${modelUid}`, request)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  removeModel(modelUid: string, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .delete<Message>(`${this.gatewayUri}/api/equipment/models/${modelUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getInstances(snackBar: MatSnackBar | null = null): Observable<EquipmentInstance[]> {
    return this.http
      .get<EquipmentInstance[]>(`${this.gatewayUri}/api/equipment/instances`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getModelInstances(modelUid: string, snackBar: MatSnackBar | null = null): Observable<EquipmentInstance[]> {
    return this.http
      .get<EquipmentInstance[]>(`${this.gatewayUri}/api/equipment/models/${modelUid}/instances`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getActiveModelInstances(modelUid: string, snackBar: MatSnackBar | null = null): Observable<EquipmentInstance[]> {
    return this.http
      .get<EquipmentInstance[]>(`${this.gatewayUri}/api/equipment/models/${modelUid}/instances/active`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getInstance(instanceUid: string, snackBar: MatSnackBar | null = null): Observable<EquipmentInstance> {
    return this.http
      .get<EquipmentInstance>(`${this.gatewayUri}/api/equipment/instances/${instanceUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  addInstance(
    modelUid: string,
    request: EquipmentInstanceRequest,
    snackBar: MatSnackBar | null = null
  ): Observable<EquipmentInstance> {
    return this.http
      .post<EquipmentInstance>(`${this.gatewayUri}/api/equipment/models/${modelUid}/instances`, request)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  updateInstance(
    instanceUid: string,
    request: EquipmentInstanceRequest,
    snackBar: MatSnackBar | null = null
  ): Observable<EquipmentInstance> {
    return this.http
      .patch<EquipmentInstance>(`${this.gatewayUri}/api/equipment/instances/${instanceUid}`, request)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  removeInstance(instanceUid: string, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .delete<Message>(`${this.gatewayUri}/api/equipment/instances/${instanceUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getModelParams(modelUid: string, snackBar: MatSnackBar | null = null): Observable<Param[]> {
    return this.http
      .get<Param[]>(`${this.gatewayUri}/api/equipment/models/${modelUid}/params`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getInstanceParams(instanaceUid: string, snackBar: MatSnackBar | null = null): Observable<Param[]> {
    return this.http
      .get<Param[]>(`${this.gatewayUri}/api/equipment/instances/${instanaceUid}/params`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  addParam(modelUid: string, request: ParamRequest, snackBar: MatSnackBar | null = null): Observable<Param> {
    return this.http
      .post<Param>(`${this.gatewayUri}/api/equipment/models/${modelUid}/params`, request)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  updateParam(paramUid: string, request: ParamRequest, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .patch<Message>(`${this.gatewayUri}/api/equipment/params/${paramUid}`, request)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  removeParam(paramUid: string, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .delete<Message>(`${this.gatewayUri}/api/equipment/params/${paramUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }
}
