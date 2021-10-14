import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { MatMenuModule } from '@angular/material/menu';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MonitorsTableComponent } from './monitors-table/monitors-table.component';
import { CreateMonitorComponent } from './create-monitor/create-monitor.component';
import { AddParamComponent } from './add-param/add-param.component';
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
    MatInputModule,
    MatSelectModule,
    GeneralModule,
  ],
  declarations: [MonitorsTableComponent, MonitorInfoComponent, CreateMonitorComponent, AddParamComponent],
})
export class MonitoringModule {}
