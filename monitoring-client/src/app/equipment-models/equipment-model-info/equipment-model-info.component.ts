import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { EquipmentService } from 'src/app/services/equipment.service';

import { EquipmentModel } from 'src/app/models/equipment-model';
import { Param } from 'src/app/models/param';
import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { InstanceStatus } from 'src/app/models/instance-status';

@Component({
  selector: 'app-equipment-model-info',
  templateUrl: './equipment-model-info.component.html',
  styleUrls: ['./equipment-model-info.component.css'],
})
export class EquipmentModelInfoComponent implements OnInit {
  modelUid: string;
  model$: Observable<EquipmentModel>;
  params$: Observable<Param[]>;
  instances$: Observable<EquipmentInstance[]>;

  constructor(private route: ActivatedRoute, private equipmentService: EquipmentService) {}

  ngOnInit() {
    this.modelUid = this.route.snapshot.params['uid'];
    
    this.model$ = of(
      new EquipmentModel(
        uuid(),
        'model_1',
        'description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1 description_1'
      )
    ); //this.equipmentService.getModel(this.modelUid);

    this.params$ = of([
      new Param(uuid(), 'Length', 'm'),
      new Param(uuid(), 'Heigth', 'm'),
      new Param(uuid(), 'Width', 'm'),
      new Param(uuid(), 'Weigth', 'kg'),
      new Param(uuid(), 'Speed', 'm/s'),
    ]);

    this.instances$ = of([
      new EquipmentInstance(uuid(), uuid(), 'instance1', 'description of instance 1', InstanceStatus.Active),
      new EquipmentInstance(uuid(), uuid(), 'instance2', 'description of instance 2', InstanceStatus.Inactive),
      new EquipmentInstance(uuid(), uuid(), 'instance3', 'description of instance 3', InstanceStatus.Active),
      new EquipmentInstance(uuid(), uuid(), 'instance4', 'description of instance 4', InstanceStatus.Decommissioned),
      new EquipmentInstance(uuid(), uuid(), 'instance5', 'description of instance 5', InstanceStatus.Active),
      new EquipmentInstance(uuid(), uuid(), 'instance1', 'description of instance 1', InstanceStatus.Active),
      new EquipmentInstance(uuid(), uuid(), 'instance2', 'description of instance 2', InstanceStatus.Inactive),
      new EquipmentInstance(uuid(), uuid(), 'instance3', 'description of instance 3', InstanceStatus.Active),
      new EquipmentInstance(uuid(), uuid(), 'instance4', 'description of instance 4', InstanceStatus.Decommissioned),
      new EquipmentInstance(uuid(), uuid(), 'instance5', 'description of instance 5', InstanceStatus.Active),
    ]);
  }
}
