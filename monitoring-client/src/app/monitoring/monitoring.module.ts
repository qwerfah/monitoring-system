import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { MatMenuModule } from '@angular/material/menu';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MonitorsTableComponent } from './monitors-table/monitors-table.component';
import { CreateMonitorComponent } from './create-monitor/create-monitor.component';
import { MonitorInfoComponent } from './monitor-info/monitor-info.component';
import { MonitoringRoutingModule } from './monitoring-routing.module';
import { PipeModule } from '../pipes/pipes.module';
import { GeneralModule } from '../general/general.module';

@NgModule({
  imports: [
    CommonModule,
    MonitoringRoutingModule,
    PipeModule,
    FormsModule,
    ReactiveFormsModule,
    MatMenuModule,
    NgxChartsModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatSnackBarModule,
    GeneralModule,
  ],
  declarations: [MonitorsTableComponent, MonitorInfoComponent, CreateMonitorComponent],
})
export class MonitoringModule {}
