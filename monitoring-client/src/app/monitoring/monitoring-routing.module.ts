import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from '../helpers/auth.guard';
import { UserRole } from '../models/user-role';
import { MonitorInfoComponent } from './monitor-info/monitor-info.component';
import { MonitorsTableComponent } from './monitors-table/monitors-table.component';

const routes: Routes = [
  {
    path: 'monitors',
    component: MonitorsTableComponent,
    canActivate: [AuthGuard],
    data: {
      roles: [UserRole.SystemAdmin, UserRole.EquipmentAdmin, UserRole.EquipmentUser],
    },
  },
  {
    path: 'monitors/:uid',
    component: MonitorInfoComponent,

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
export class MonitoringRoutingModule {}
