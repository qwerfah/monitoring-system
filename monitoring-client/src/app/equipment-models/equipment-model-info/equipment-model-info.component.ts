import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { zip } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { EquipmentService } from 'src/app/services/equipment.service';

import { EquipmentModel } from 'src/app/models/equipment-model';
import { Param } from 'src/app/models/param';
import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { InstanceStatus } from 'src/app/models/instance-status';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { mergeAll } from 'rxjs/operators';
import { FileMeta } from 'src/app/models/file-meta';
import { DocumentationService } from 'src/app/services/documentation.service';
import { UserRole } from 'src/app/models/user-role';
import { UserWithToken } from 'src/app/models/user';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-equipment-model-info',
  templateUrl: './equipment-model-info.component.html',
  styleUrls: ['./equipment-model-info.component.css'],
})
export class EquipmentModelInfoComponent implements OnInit {
  isLoading: boolean = true;
  modelUid: string;

  model: EquipmentModel;
  files: FileMeta[];
  params: Param[];
  instances: EquipmentInstance[];

  UserRole = UserRole;

  currentUser: UserWithToken | undefined = undefined;

  constructor(
    private route: ActivatedRoute,
    private sessionService: SessionService,
    private equipmentService: EquipmentService,
    private documentationService: DocumentationService,
    private snackBar: MatSnackBar
  ) {
    sessionService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  ngOnInit() {
    this.modelUid = this.route.snapshot.params['uid'];

    zip(
      this.equipmentService.getModel(this.modelUid),
      this.equipmentService.getModelParams(this.modelUid),
      this.equipmentService.getModelInstances(this.modelUid)
    ).subscribe(
      (result) => {
        this.model = result[0];
        this.params = result[1];
        this.instances = result[2];
        this.isLoading = false;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис оборудования недоступен', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: модель не найдена', 'Ок');
          }
        }
        this.isLoading = false;
      }
    );

    this.documentationService.getFilesMeta(this.modelUid).subscribe(
      (files) => {
        this.files = files;
        this.isLoading = false;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис документации недоступен', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: модель не найдена', 'Ок');
          }
        }
        this.isLoading = false;
      }
    );
  }

  /** Check if current logged in user has sufficient rights to access element.
   * @param roles Array of sufficient user roles.
   * @returns True if current user role is sufficient, otherwise false.
   */
  isAllowed(roles: UserRole[]): boolean {
    return this.currentUser !== undefined && roles.indexOf(this.currentUser.role) !== -1;
  }

  downloadFile(file: FileMeta) {
    this.documentationService.getFile(file.uid).subscribe((blob) => {
      // It is necessary to create a new blob object with mime-type explicitly set
      // otherwise only Chrome works like it should
      var newBlob = new Blob([blob], { type: file.contentType });

      const data = window.URL.createObjectURL(newBlob);

      var link = document.createElement('a');
      link.href = data;
      link.download = file.filename;
      // this is necessary as link.click() does not work on the latest firefox
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

      setTimeout(function () {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    });
  }

  uploadFile(event) {
    let fileList: FileList = event.target.files;
    if (fileList.length > 0) {
      let file: File = fileList[0];
      this.isLoading = true;
      this.documentationService.uploadFile(this.modelUid, file).subscribe(
        (file) => {
          this.isLoading = false;
          this.files.push(file);
          this.snackBar.open('Успех: файл загружен', 'Ок');
        },
        (err: HttpErrorResponse) => {
          switch (err.status) {
            case 0: {
              this.snackBar.open('Ошибка загрузки файла: отсутсвтует соединение с сервером', 'Ок');
              break;
            }
            case 502: {
              this.snackBar.open('Ошибка загрузки файла: сервис документации недоступен', 'Ок');
              break;
            }
            case 404: {
              this.snackBar.open('Ошибка загрузки файла: модель не найдена', 'Ок');
            }
          }
          this.isLoading = false;
        }
      );
    }
  }

  removeFile(fileUid: string): void {
    if (!confirm(`Удалить файл ${this.files.find((u) => u.uid === fileUid)?.filename}?`)) return;

    this.documentationService.removeFile(fileUid).subscribe(
      (msg) => {
        this.files.splice(
          this.files.findIndex((f) => f.uid === fileUid),
          1
        );
        this.snackBar.open('Успех: файл удален', 'Ок');
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка удаления файла: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка удаления файла: сервис документации недоступен', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка удаления файла: файл не найден', 'Ок');
          }
        }
      }
    );
  }

  removeParam(paramUid: string): void {
    if (!confirm(`Удалить параметр ${this.params.find((u) => u.uid === paramUid)?.name}?`)) return;

    this.equipmentService.removeParam(paramUid).subscribe(
      (msg) => {
        this.params.splice(
          this.params.findIndex((p) => p.uid === paramUid),
          1
        );
        this.snackBar.open('Успех: параметр удален', 'Ок');
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка удаления параметра: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка удаления параметра: сервис оборудования недоступен', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка удаления параметра: параметр не найден', 'Ок');
          }
        }
        this.isLoading = false;
      }
    );
  }
}
