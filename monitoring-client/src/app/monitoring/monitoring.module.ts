import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatMenuModule } from '@angular/material/menu';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MonitorsTableComponent } from './monitors-table/monitors-table.component';
import { MonitorInfoComponent } from './monitor-info/monitor-info.component';
import { CreateMonitorComponent } from './create-monitor/create-monitor.component';
import { MonitoringRoutingModule } from './monitoring-routing.module';
import { PipeModule } from '../pipes/pipes.module';

@NgModule({
  imports: [CommonModule, MonitoringRoutingModule, PipeModule, FormsModule, ReactiveFormsModule, MatMenuModule],
  declarations: [MonitorsTableComponent, MonitorInfoComponent, CreateMonitorComponent],
})
export class MonitoringModule {}
