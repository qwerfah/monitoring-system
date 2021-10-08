import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of, zip } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { EquipmentService } from 'src/app/services/equipment.service';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { Monitor } from 'src/app/models/monitor';
import { Param } from 'src/app/models/param';
import { InstanceStatus } from 'src/app/models/instance-status';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-equipment-instance-info',
  templateUrl: './equipment-instance-info.component.html',
  styleUrls: ['./equipment-instance-info.component.css'],
})
export class EquipmentInstanceInfoComponent implements OnInit {
  isLoading: boolean = true;

  instanceUid: string;
  instance: EquipmentInstance;
  params: Param[];
  monitors$: Observable<Monitor[]>;

  constructor(
    private route: ActivatedRoute,
    private equipmentService: EquipmentService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.instanceUid = this.route.snapshot.params['uid'];

    zip(
      this.equipmentService.getInstance(this.instanceUid),
      this.equipmentService.getInstanceParams(this.instanceUid)
    ).subscribe(
      (result) => {
        this.instance = result[0];
        this.params = result[1];
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
            this.snackBar.open('Ошибка: данные не найдены', 'Ок');
          }
        }
        this.isLoading = false;
      },
      () => {
        this.isLoading = false;
      }
    );

    this.monitors$ = of([
      new Monitor(
        uuid(),
        uuid(),
        uuid(),
        'Equipment instance monitor 1',
        'instance_1',
        'model_1',
        'Description of equipment instance monitor 1'
      ),
      new Monitor(
        uuid(),
        uuid(),
        uuid(),
        'Equipment instance monitor 2',
        'instance_2',
        'model_5',
        'Description of equipment instance monitor 2'
      ),
      new Monitor(
        uuid(),
        uuid(),
        uuid(),
        'Equipment instance monitor 3',
        'instance_3',
        'model_2',
        'Description of equipment instance monitor 3'
      ),
      new Monitor(
        uuid(),
        uuid(),
        uuid(),
        'Equipment instance monitor 4',
        'instance_4',
        'model_3',
        'Description of equipment instance monitor 4'
      ),
      new Monitor(
        uuid(),
        uuid(),
        uuid(),
        'Equipment instance monitor 5',
        'instance_5',
        'model_6',
        'Description of equipment instance monitor 5'
      ),
      new Monitor(
        uuid(),
        uuid(),
        uuid(),
        'Equipment instance monitor 6',
        'instance_6',
        'model_4',
        'Description of equipment instance monitor 6'
      ),
    ]);
  }
}
