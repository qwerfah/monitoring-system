import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MainFormComponent } from './main-form/main-form.component';

const routes: Routes = [
  {
    path: '',
    component: MainFormComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GenealRoutingModule {}
