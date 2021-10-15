import { Component, OnInit } from '@angular/core';
import { v4 as uuid } from 'uuid';

import { EquipmentInstance } from 'src/app/models/equipment-instance';
import { InstanceStatus } from 'src/app/models/instance-status';
import { EquipmentService } from 'src/app/services/equipment.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EquipmentInstanceRequest } from 'src/app/models/equipment-instance-request';
import { UserRole } from 'src/app/models/user-role';
import { UserWithToken } from 'src/app/models/user';
import { SessionService } from 'src/app/services/session.service';
import { UserDependentComponent } from 'src/app/helpers/user-dependent.component';

@Component({
  selector: 'app-equipment-instances-table',
  templateUrl: './equipment-instances-table.component.html',
  styleUrls: ['./equipment-instances-table.component.css'],
})
export class EquipmentInstancesTableComponent extends UserDependentComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;

  instances: EquipmentInstance[];

  constructor(
    sessionService: SessionService,
    private equipmentService: EquipmentService,
    private snackBar: MatSnackBar
  ) {
    super(sessionService);

    equipmentService.getInstances().subscribe(
      (instances) => {
        this.instances = instances;
        this.isLoading = false;
      },
      (err) => (this.isLoading = false)
    );
  }

  ngOnInit() {}

  openModal() {
    this.isAdding = true;
  }

  addInstance(instnace: [EquipmentInstanceRequest, string] | null) {
    this.isAdding = false;

    if (instnace === null) return;

    this.isLoading = true;

    this.equipmentService.addInstance(instnace[1], instnace[0], this.snackBar).subscribe(
      (instnace) => {
        this.snackBar.open('Успех: экземпляр добавлен', 'Ок', { duration: 5000 });
        this.instances.push(instnace);
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }

  removeInstance(instanceUid: string): void {
    if (!confirm(`Удалить экземпляр ${this.instances.find((u) => u.uid === instanceUid)?.name}?`)) return;

    this.equipmentService.removeInstance(instanceUid, this.snackBar).subscribe(
      () => {
        this.instances.splice(
          this.instances.findIndex((i) => i.uid === instanceUid),
          1
        );
        this.snackBar.open('Успех: экземпляр удален', 'Ок', { duration: 5000 });
      },
      () => (this.isLoading = false)
    );
  }
}
