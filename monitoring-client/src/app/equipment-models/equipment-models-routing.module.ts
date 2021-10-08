import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EquipmentModelsTableComponent } from './equipment-models-table/equipment-models-table.component';
import { EquipmentModelInfoComponent } from './equipment-model-info/equipment-model-info.component';
import { AuthGuard } from '../helpers/auth.guard';
import { UserRole } from '../models/user-role';

const routes: Routes = [
  {
    path: 'models',
    component: EquipmentModelsTableComponent,
    canActivate: [AuthGuard],
    data: {
      roles: [UserRole.SystemAdmin, UserRole.EquipmentAdmin, UserRole.EquipmentUser],
    },
  },
  {
    path: 'models/:uid',
    component: EquipmentModelInfoComponent,
    canActivate: [AuthGuard],
    data: {
      roles: [UserRole.SystemAdmin, UserRole.EquipmentAdmin, UserRole.EquipmentUser],
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EquipmentModelsRoutingModule {}
