import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { FileMeta } from '../models/file-meta';
import { Message } from '../models/message';

@Injectable({
  providedIn: 'root',
})
export class DocumentationService {
  constructor(private http: HttpClient, @Inject('GATEWAY_URI') private gatewayUri: string) {}

  getFilesMeta(modelUid: string): Observable<FileMeta[]> {
    return this.http.get<FileMeta[]>(`${this.gatewayUri}/api/documentation/models/${modelUid}/files`);
  }

  getFile(fileUid: string): Observable<Blob> {
    return this.http.get<Blob>(`${this.gatewayUri}/api/documentation/files/${fileUid}`, {
      responseType: 'blob' as 'json',
    });
  }

  uploadFile(modelUid: string, file: File): Observable<FileMeta> {
    let formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post<FileMeta>(`${this.gatewayUri}/api/documentation/models/${modelUid}/files`, formData);
  }

  removeFile(fileUid: string): Observable<Message> {
    return this.http.delete<Message>(`${this.gatewayUri}/api/documentation/files/${fileUid}`);
  }
}
