import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { Monitor } from 'src/app/models/monitor';
import { Param } from 'src/app/models/param';
import { ParamValue } from 'src/app/models/param-value';

@Component({
  selector: 'app-monitor-info',
  templateUrl: './monitor-info.component.html',
  styleUrls: ['./monitor-info.component.css'],
})
export class MonitorInfoComponent implements OnInit {
  monitorUid: string;
  monitor$: Observable<Monitor>;
  params$: Observable<Param[]>;
  paramValues$: Observable<ParamValue[]>;

  constructor() {
    this.monitor$ = of(new Monitor(uuid(), uuid(), uuid(), 'monitor_1', 'instance_1', 'model_1', 'description'));
    this.params$ = of([
      new Param(uuid(), uuid(), 'Length', 'm'),
      new Param(uuid(), uuid(), 'Heigth', 'm'),
      new Param(uuid(), uuid(), 'Width', 'm'),
      new Param(uuid(), uuid(), 'Weigth', 'kg'),
      new Param(uuid(), uuid(), 'Speed', 'm/s'),
    ]);
  }

  ngOnInit() {}
}
