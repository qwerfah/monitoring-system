import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EquipmentModelsComponent } from './equipment-models.component';
import { EquipmentModelsRoutingModule } from './equipment-models-routing.module';

import { PipeModule } from '../pipes/pipes.module';

@NgModule({
  imports: [CommonModule, EquipmentModelsRoutingModule, PipeModule],
  declarations: [EquipmentModelsComponent],
})
export class EquipmentModelsModule {}
