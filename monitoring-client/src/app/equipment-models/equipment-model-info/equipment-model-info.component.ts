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
import { ParamRequest } from 'src/app/models/param-request';
import { UserDependentComponent } from 'src/app/helpers/user-dependent.component';

@Component({
  selector: 'app-equipment-model-info',
  templateUrl: './equipment-model-info.component.html',
  styleUrls: ['./equipment-model-info.component.css'],
})
export class EquipmentModelInfoComponent extends UserDependentComponent implements OnInit {
  isLoading: boolean = true;
  isAdding: boolean = false;

  modelUid: string;

  model: EquipmentModel;
  files: FileMeta[];
  params: Param[];
  instances: EquipmentInstance[];

  constructor(
    private route: ActivatedRoute,
    sessionService: SessionService,
    private equipmentService: EquipmentService,
    private documentationService: DocumentationService,
    private snackBar: MatSnackBar
  ) {
    super(sessionService);
  }

  ngOnInit() {
    this.modelUid = this.route.snapshot.params['uid'];

    zip(
      this.equipmentService.getModel(this.modelUid, this.snackBar),
      this.equipmentService.getModelParams(this.modelUid, this.snackBar),
      this.equipmentService.getModelInstances(this.modelUid, this.snackBar)
    ).subscribe(
      (result) => {
        this.model = result[0];
        this.params = result[1];
        this.instances = result[2];
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );

    this.documentationService.getFilesMeta(this.modelUid, this.snackBar).subscribe(
      (files) => {
        this.files = files;
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }

  openModal(): void {
    this.isAdding = true;
  }

  addParam(param: ParamRequest | null): void {
    this.isAdding = false;

    if (param === null) return;

    this.isLoading = true;

    this.equipmentService.addParam(this.modelUid, param, this.snackBar).subscribe(
      (param) => {
        this.params.push(param);
        this.isLoading = false;
        this.snackBar.open('Успех: параметр добавлен', 'Ок');
      },
      () => (this.isLoading = false)
    );
  }

  downloadFile(file: FileMeta): void {
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
      this.documentationService.uploadFile(this.modelUid, file, this.snackBar).subscribe(
        (file) => {
          this.isLoading = false;
          this.files.push(file);
          this.snackBar.open('Успех: файл загружен', 'Ок', { duration: 5000 });
        },
        () => (this.isLoading = false)
      );
    }
  }

  removeFile(fileUid: string): void {
    if (!confirm(`Удалить файл ${this.files.find((u) => u.uid === fileUid)?.filename}?`)) return;

    this.documentationService.removeFile(fileUid, this.snackBar).subscribe(() => {
      this.files.splice(
        this.files.findIndex((f) => f.uid === fileUid),
        1
      );
      this.snackBar.open('Успех: файл удален', 'Ок', { duration: 5000 });
    });
  }

  removeParam(paramUid: string): void {
    if (!confirm(`Удалить параметр ${this.params.find((u) => u.uid === paramUid)?.name}?`)) return;

    this.equipmentService.removeParam(paramUid, this.snackBar).subscribe(
      () => {
        this.params.splice(
          this.params.findIndex((p) => p.uid === paramUid),
          1
        );
        this.snackBar.open('Успех: параметр удален', 'Ок', { duration: 5000 });
      },
      () => (this.isLoading = false)
    );
  }
}
