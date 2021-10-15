import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { FileMeta } from '../models/file-meta';
import { Message } from '../models/message';
import { ServiceBase } from './service.base';

@Injectable({
  providedIn: 'root',
})
export class DocumentationService extends ServiceBase {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {
    super();
  }

  getFilesMeta(modelUid: string, snackBar: MatSnackBar | null = null): Observable<FileMeta[]> {
    return this.http
      .get<FileMeta[]>(`${this.gatewayUri}/api/documentation/models/${modelUid}/files`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  getFile(fileUid: string, snackBar: MatSnackBar | null = null): Observable<Blob> {
    return this.http
      .get<Blob>(`${this.gatewayUri}/api/documentation/files/${fileUid}`, {
        responseType: 'blob' as 'json',
      })
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  uploadFile(modelUid: string, file: File, snackBar: MatSnackBar | null = null): Observable<FileMeta> {
    let formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.http
      .post<FileMeta>(`${this.gatewayUri}/api/documentation/models/${modelUid}/files`, formData)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }

  removeFile(fileUid: string, snackBar: MatSnackBar | null = null): Observable<Message> {
    return this.http
      .delete<Message>(`${this.gatewayUri}/api/documentation/files/${fileUid}`)
      .pipe(catchError(this.baseErrorHandler(snackBar)));
  }
}
