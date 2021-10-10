import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../helpers/auth.guard';
import { UserRole } from '../models/user-role';

import { MainFormComponent } from './main-form/main-form.component';
import { MainPageComponent } from './main-page/main-page.component';

const routes: Routes = [
  {
    path: '',
    component: MainPageComponent,
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
export class GenealRoutingModule {}
