import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EquipmentInstancesTableComponent } from './equipment-instances-table/equipment-instances-table.component';
import { EquipmentInstanceInfoComponent } from './equipment-instance-info/equipment-instance-info.component';

const routes: Routes = [
  {
    path: 'instances',
    component: EquipmentInstancesTableComponent,
  },
  {
    path: 'instances/:uid',
    component: EquipmentInstanceInfoComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EquipmentInstancesRoutingModule {}
