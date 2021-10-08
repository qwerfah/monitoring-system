import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { EquipmentModelsTableComponent } from './equipment-models-table/equipment-models-table.component';
import { EquipmentModelInfoComponent } from './equipment-model-info/equipment-model-info.component';
import { CreateEquipmentModelComponent } from './create-equipment-model/create-equipment-model.component';
import { EquipmentModelsRoutingModule } from './equipment-models-routing.module';

import { PipeModule } from '../pipes/pipes.module';
import { GeneralModule } from '../general/general.module';

@NgModule({
  imports: [
    CommonModule,
    EquipmentModelsRoutingModule,
    PipeModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    GeneralModule
  ],
  declarations: [EquipmentModelsTableComponent, EquipmentModelInfoComponent, CreateEquipmentModelComponent],
})
export class EquipmentModelsModule {}
