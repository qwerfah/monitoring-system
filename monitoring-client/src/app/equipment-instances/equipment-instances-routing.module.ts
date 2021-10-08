import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EquipmentInstancesTableComponent } from './equipment-instances-table/equipment-instances-table.component';
import { EquipmentInstanceInfoComponent } from './equipment-instance-info/equipment-instance-info.component';
import { AuthGuard } from '../helpers/auth.guard';
import { UserRole } from '../models/user-role';

const routes: Routes = [
  {
    path: 'instances',
    component: EquipmentInstancesTableComponent,
    canActivate: [AuthGuard],
    data: {
      roles: [UserRole.SystemAdmin, UserRole.EquipmentAdmin, UserRole.EquipmentUser],
    },
  },
  {
    path: 'instances/:uid',
    component: EquipmentInstanceInfoComponent,
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
export class EquipmentInstancesRoutingModule {}
