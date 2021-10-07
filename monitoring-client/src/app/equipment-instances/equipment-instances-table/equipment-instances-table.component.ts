import { Component, OnInit } from '@angular/core';
import { v4 as uuid } from 'uuid';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { InstanceStatus } from 'src/app/models/instance-status';

@Component({
  selector: 'app-equipment-instances-table',
  templateUrl: './equipment-instances-table.component.html',
  styleUrls: ['./equipment-instances-table.component.css'],
})
export class EquipmentInstancesTableComponent implements OnInit {
  isAdding: boolean = false;

  instances: EquipmentInstance[] = [
    new EquipmentInstance(uuid(), uuid(), 'instance1', 'model_1', 'description of instance 1', InstanceStatus.Active),
    new EquipmentInstance(uuid(), uuid(), 'instance2', 'model_2', 'description of instance 2', InstanceStatus.Inactive),
    new EquipmentInstance(uuid(), uuid(), 'instance3', 'model_3', 'description of instance 3', InstanceStatus.Active),
    new EquipmentInstance(
      uuid(),
      uuid(),
      'instance4',
      'model_4',
      'description of instance 4',
      InstanceStatus.Decommissioned
    ),
    new EquipmentInstance(uuid(), uuid(), 'instance5', 'model_5', 'description of instance 5', InstanceStatus.Active),
    new EquipmentInstance(uuid(), uuid(), 'instance1', 'model_6', 'description of instance 1', InstanceStatus.Active),
    new EquipmentInstance(uuid(), uuid(), 'instance2', 'model_7', 'description of instance 2', InstanceStatus.Inactive),
    new EquipmentInstance(uuid(), uuid(), 'instance3', 'model_8', 'description of instance 3', InstanceStatus.Active),
    new EquipmentInstance(
      uuid(),
      uuid(),
      'instance4',
      'model_9',
      'description of instance 4',
      InstanceStatus.Decommissioned
    ),
    new EquipmentInstance(uuid(), uuid(), 'instance5', 'model_10', 'description of instance 5', InstanceStatus.Active),
  ];

  constructor() {}

  ngOnInit() {}

  openModal() {
    this.isAdding = true;
  }

  addInstance(instnace: EquipmentInstance | null) {
    this.isAdding = false;
  }
}
