import { Component, Directive, ElementRef, Inject, Input, OnInit, ViewChild } from '@angular/core';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { Monitor } from 'src/app/models/monitor';
import { MonitorRequest } from 'src/app/models/monitor-request';

@Component({
  selector: 'app-monitors-table',
  templateUrl: './monitors-table.component.html',
  styleUrls: ['./monitors-table.component.css'],
})
export class MonitorsTableComponent implements OnInit {
  isAdding: boolean = false;
  monitors$: Observable<Monitor[]>;

  constructor() {
    this.monitors$ = of([
      new Monitor(uuid(), uuid(), uuid(), 'monitor_1', 'instance_1', 'model_1', 'description of monitor_1'),
      new Monitor(uuid(), uuid(), uuid(), 'monitor_2', 'instance_2', 'model_2', 'description of monitor_2'),
      new Monitor(uuid(), uuid(), uuid(), 'monitor_3', 'instance_3', 'model_3', 'description of monitor_3'),
      new Monitor(uuid(), uuid(), uuid(), 'monitor_4', 'instance_4', 'model_4', 'description of monitor_4'),
      new Monitor(uuid(), uuid(), uuid(), 'monitor_5', 'instance_5', 'model_5', 'description of monitor_5'),
    ]);
  }

  ngOnInit() {}

  openModal() {
    this.isAdding = true;
  }

  addMonitor(monitor: MonitorRequest | null) {
    this.isAdding = false;
  }
}
