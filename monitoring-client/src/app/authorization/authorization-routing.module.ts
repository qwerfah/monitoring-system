import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthorizationFormComponent } from './authorization-form/authorization-form.component';

const routes: Routes = [
  {
    path: 'login',
    component: AuthorizationFormComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthorizationRoutingModule {}
