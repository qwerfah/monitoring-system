import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PipeModule } from '../pipes/pipes.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatMenuModule } from '@angular/material/menu';

import { EquipmentInstancesRoutingModule } from './equipment-instances-routing.module';
import { EquipmentInstanceInfoComponent } from './equipment-instance-info/equipment-instance-info.component';
import { EquipmentInstancesTableComponent } from './equipment-instances-table/equipment-instances-table.component';
import { CreateEquipmentInstanceComponent } from './create-equipment-instance/create-equipment-instance.component';

@NgModule({
  imports: [
    CommonModule,
    PipeModule,
    HttpClientModule,
    EquipmentInstancesRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatMenuModule,
  ],
  declarations: [EquipmentInstancesTableComponent, EquipmentInstanceInfoComponent, CreateEquipmentInstanceComponent],
})
export class EquipmentInstancesModule {}
