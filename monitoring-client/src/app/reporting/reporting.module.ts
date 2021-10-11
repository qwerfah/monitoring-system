import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportingInfoComponent } from './reporting-info/reporting-info.component';
import { ReportingRoutingModule } from './reporting-routing.module';
import { GeneralModule } from '../general/general.module';
import { PipeModule } from '../pipes/pipes.module';

@NgModule({
  imports: [CommonModule, GeneralModule, PipeModule, ReportingRoutingModule],
  declarations: [ReportingInfoComponent],
})
export class ReportingModule {}
