import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EquipmentModelsTableComponent } from './equipment-models-table/equipment-models-table.component';
import { EquipmentModelInfoComponent } from './equipment-model-info/equipment-model-info.component';

const routes: Routes = [
  {
    path: 'models',
    component: EquipmentModelsTableComponent,
  },
  {
    path: 'models/:uid',
    component: EquipmentModelInfoComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EquipmentModelsRoutingModule {}
