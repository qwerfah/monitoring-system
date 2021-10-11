import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ReportingInfoComponent } from './reporting-info/reporting-info.component';

const routes: Routes = [
  {
    path: 'reporting',
    component: ReportingInfoComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReportingRoutingModule {}
