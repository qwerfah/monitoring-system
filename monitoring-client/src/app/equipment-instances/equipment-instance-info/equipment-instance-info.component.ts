import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { EquipmentService } from 'src/app/services/equipment.service';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { Monitor } from 'src/app/models/monitor';
import { Param } from 'src/app/models/param';
import { InstanceStatus } from 'src/app/models/instance-status';

@Component({
  selector: 'app-equipment-instance-info',
  templateUrl: './equipment-instance-info.component.html',
  styleUrls: ['./equipment-instance-info.component.css'],
})
export class EquipmentInstanceInfoComponent implements OnInit {
  instanceUid: string;
  instance$: Observable<EquipmentInstance>;
  params$: Observable<Param[]>;
  monitors$: Observable<Monitor[]>;

  constructor(private route: ActivatedRoute, private equipmentService: EquipmentService) {}

  ngOnInit() {
    this.instanceUid = this.route.snapshot.params['uid'];

    this.instance$ = of(
      new EquipmentInstance(
        uuid(),
        uuid(),
        'Equipment instance',
        'Description of equipment instance',
        InstanceStatus.Active
      )
    );

    this.params$ = of([
      new Param(uuid(), 'Length', 'm'),
      new Param(uuid(), 'Heigth', 'm'),
      new Param(uuid(), 'Width', 'm'),
      new Param(uuid(), 'Weigth', 'kg'),
      new Param(uuid(), 'Speed', 'm/s'),
    ]);

    this.monitors$ = of([
      new Monitor(uuid(), uuid(), 'Equipment instance monitor 1', 'Description of equipment instance monitor 1'),
      new Monitor(uuid(), uuid(), 'Equipment instance monitor 2', 'Description of equipment instance monitor 2'),
      new Monitor(uuid(), uuid(), 'Equipment instance monitor 3', 'Description of equipment instance monitor 3'),
      new Monitor(uuid(), uuid(), 'Equipment instance monitor 4', 'Description of equipment instance monitor 4'),
      new Monitor(uuid(), uuid(), 'Equipment instance monitor 5', 'Description of equipment instance monitor 5'),
      new Monitor(uuid(), uuid(), 'Equipment instance monitor 6', 'Description of equipment instance monitor 6'),
    ]);
  }
}
