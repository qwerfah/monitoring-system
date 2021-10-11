import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserTableComponent } from './user-table/user-table.component';

const routes: Routes = [
  {
    path: 'users',
    component: UserTableComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdministrationRoutingModule {}
