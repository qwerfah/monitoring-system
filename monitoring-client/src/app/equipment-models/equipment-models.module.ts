import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { EquipmentModelsTableComponent } from './equipment-models-table/equipment-models-table.component';
import { EquipmentModelInfoComponent } from './equipment-model-info/equipment-model-info.component';
import { EquipmentModelsRoutingModule } from './equipment-models-routing.module';

import { PipeModule } from '../pipes/pipes.module';

@NgModule({
  imports: [CommonModule, EquipmentModelsRoutingModule, PipeModule, HttpClientModule],
  declarations: [EquipmentModelsTableComponent, EquipmentModelInfoComponent],
})
export class EquipmentModelsModule {}
