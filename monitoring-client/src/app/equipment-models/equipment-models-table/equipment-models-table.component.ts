import { Component, OnInit } from '@angular/core';

import { EquipmentModel } from '../../models/equipment-model';

import { EquipmentService } from 'src/app/services/equipment.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EquipmentModelRequest } from 'src/app/models/equipment-model-request';
import { SessionService } from 'src/app/services/session.service';
import { UserDependentComponent } from 'src/app/helpers/user-dependent.component';

@Component({
  selector: 'app-equipment-models',
  templateUrl: './equipment-models-table.component.html',
  styleUrls: ['./equipment-models-table.component.css'],
})
export class EquipmentModelsTableComponent extends UserDependentComponent implements OnInit {
  isAdding: boolean = false;
  isLoading: boolean = true;
  models: EquipmentModel[];

  constructor(
    sessionService: SessionService,
    private equipmentService: EquipmentService,
    private snackBar: MatSnackBar
  ) {
    super(sessionService);
  }

  ngOnInit() {
    this.equipmentService.getModels(this.snackBar).subscribe(
      (models) => {
        this.models = models;
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }

  openModal() {
    this.isAdding = true;
  }

  addModel(model: EquipmentModelRequest | null) {
    this.isAdding = false;
    if (model === null) return;
    this.isLoading = true;

    this.equipmentService.addModel(model, this.snackBar).subscribe(
      (model) => {
        this.snackBar.open('Успех: модель добавлена', 'Ок', { duration: 5000 });
        this.models.push(model);
        this.isLoading = false;
      },
      () => (this.isLoading = false)
    );
  }

  removeModel(modelUid: string): void {
    if (!confirm(`Удалить модель ${this.models.find((u) => u.uid === modelUid)?.name}?`)) return;

    this.equipmentService.removeModel(modelUid, this.snackBar).subscribe((msg) => {
      this.models.splice(
        this.models.findIndex((m) => m.uid === modelUid),
        1
      );
      this.snackBar.open('Успех: модель удалена', 'Ок', { duration: 5000 });
    });
  }
}
