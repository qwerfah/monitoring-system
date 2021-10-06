import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EquipmentModelsTableComponent } from './equipment-models-table/equipment-models-table.component';
import { EquipmentModelInfoComponent } from './equipment-model-info/equipment-model-info.component';
import { AuthGuard } from '../helpers/auth.guard';

const routes: Routes = [
  {
    path: 'models',
    component: EquipmentModelsTableComponent,
    /*
    canActivate: [AuthGuard],
    data: {
      role: 'EquipmentUser',
    },
    */
  },
  {
    path: 'models/:uid',
    component: EquipmentModelInfoComponent,
    /*
    canActivate: [AuthGuard],
    data: {
      role: 'EquipmentUser',
    },
    */
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EquipmentModelsRoutingModule {}
