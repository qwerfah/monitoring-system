import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EquipmentModelsComponent } from './equipment-models.component';

const routes: Routes = [
  {
    path: 'models',
    component: EquipmentModelsComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EquipmentModelsRoutingModule {}
