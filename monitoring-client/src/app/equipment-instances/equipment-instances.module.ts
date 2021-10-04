import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PipeModule } from '../pipes/pipes.module';
import { HttpClientModule } from '@angular/common/http';

import { EquipmentInstancesRoutingModule } from './equipment-instances-routing.module';
import { EquipmentInstanceInfoComponent } from './equipment-instance-info/equipment-instance-info.component';
import { EquipmentInstancesTableComponent } from './equipment-instances-table/equipment-instances-table.component';

@NgModule({
  imports: [CommonModule, PipeModule, HttpClientModule, EquipmentInstancesRoutingModule],
  declarations: [EquipmentInstancesTableComponent, EquipmentInstanceInfoComponent],
})
export class EquipmentInstancesModule {}
