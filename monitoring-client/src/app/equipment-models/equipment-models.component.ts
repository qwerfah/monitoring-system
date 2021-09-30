import { Component, OnInit } from '@angular/core';

import { EquipmentModel } from '../models/equipment-model';

import { v4 as uuid } from 'uuid';

@Component({
  selector: 'app-equipment-models',
  templateUrl: './equipment-models.component.html',
  styleUrls: ['./equipment-models.component.css'],
})
export class EquipmentModelsComponent implements OnInit {
  models: EquipmentModel[] = [
    new EquipmentModel(uuid(), 'model_1', 'description_1'),
    new EquipmentModel(uuid(), 'model_1', 'description_1'),
    new EquipmentModel(uuid(), 'model_1', 'description_1'),
  ];

  constructor() {}

  ngOnInit() {}
}
