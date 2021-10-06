import { Component, OnInit } from '@angular/core';

import { EquipmentModel } from '../../models/equipment-model';

import { v4 as uuid } from 'uuid';

@Component({
  selector: 'app-equipment-models',
  templateUrl: './equipment-models-table.component.html',
  styleUrls: ['./equipment-models-table.component.css'],
})
export class EquipmentModelsTableComponent implements OnInit {
  isAdding: boolean = false;
  models: EquipmentModel[] = [
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
    new EquipmentModel(
      uuid(),
      'model_1 model_1 model_1',
      'description_1 description_1 description_1 description_1 description_1'
    ),
  ];

  constructor() {}

  ngOnInit() {}

  openModal() {
    this.isAdding = true;
  }

  addModel(model: EquipmentModel | null) {
    this.isAdding = false;
  }
}
