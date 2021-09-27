import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MainFormComponent } from './main-form/main-form.component';
import { MainPageComponent } from './main-page/main-page.component';

const routes: Routes = [
  {
    path: '',
    component: MainPageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GenealRoutingModule {}
