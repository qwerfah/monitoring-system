import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';

import { Monitor } from 'src/app/models/monitor';

@Component({
  selector: 'app-monitors-table',
  templateUrl: './monitors-table.component.html',
  styleUrls: ['./monitors-table.component.css'],
})
export class MonitorsTableComponent implements OnInit {
  monitors$: Observable<Monitor[]>;

  constructor() {
    this.monitors$ = of([
      new Monitor(uuid(), uuid(), 'monitor_1', 'description of monitor_1'),
      new Monitor(uuid(), uuid(), 'monitor_2', 'description of monitor_2'),
      new Monitor(uuid(), uuid(), 'monitor_3', 'description of monitor_3'),
      new Monitor(uuid(), uuid(), 'monitor_4', 'description of monitor_4'),
      new Monitor(uuid(), uuid(), 'monitor_5', 'description of monitor_5'),
    ]);
  }

  ngOnInit() {}
}
