import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { v4 as uuid } from 'uuid';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { Monitor } from 'src/app/models/monitor';
import { Param } from 'src/app/models/param';
import { ParamValue } from 'src/app/models/param-value';
import { tap } from 'rxjs/operators';

type ParamChartDataType = { name: string; series: { name: Date; value: number }[] };

@Component({
  selector: 'app-monitor-info',
  templateUrl: './monitor-info.component.html',
  styleUrls: ['./monitor-info.component.css'],
})
export class MonitorInfoComponent implements OnInit {
  monitorUid: string;
  instanceUid: string;

  monitor$: Observable<Monitor>;
  params$: Observable<Param[]>;
  paramValues$: Observable<ParamValue[]>;

  params: Param[];
  chartData: ParamChartDataType[];

  constructor() {
    this.monitor$ = of(new Monitor(uuid(), uuid(), uuid(), 'monitor_1', 'instance_1', 'model_1', 'description')).pipe(
      tap((monitor) => (this.instanceUid = monitor.instanceUid))
    );
    this.params$ = of([
      new Param(uuid(), uuid(), 'Length', 'm'),
      new Param(uuid(), uuid(), 'Heigth', 'm'),
      new Param(uuid(), uuid(), 'Width', 'm'),
      new Param(uuid(), uuid(), 'Weigth', 'kg'),
      new Param(uuid(), uuid(), 'Speed', 'm/s'),
    ]).pipe(
      tap((params) => {
        this.params = params;
        this.paramValues$ = of(this.generateParamValues(params)).pipe(
          tap((values) => (this.chartData = this.getParamUids(values).map((uid) => this.getParamValues(uid, values))))
        );
      })
    );
  }

  private onlyUnique(value: string, index: number, self: string[]): boolean {
    return self.indexOf(value) === index;
  }

  private getParamUids(params: ParamValue[]): string[] {
    return params.map((param) => param.paramUid).filter(this.onlyUnique);
  }

  private getParamValues(paramUid: string, params: ParamValue[]): ParamChartDataType {
    let series: { name: Date; value: number }[] = params
      .filter((param) => param.paramUid == paramUid)
      .sort((a, b) => a.time.getSeconds() - b.time.getSeconds())
      .map<{ name: Date; value: number }>((param) => {
        return {
          name: param.time,
          value: +param.value,
        };
      });

    let param = this.params.find((p) => p.uid === paramUid);
    if (param === undefined) throw new ReferenceError('No param with specified uuid');

    return { name: `${param.name}, ${param.measurmentUnits}`, series: series };
  }

  private toDateTime(secs: number): Date {
    let t = new Date(1970, 0, 1);
    t.setSeconds(secs);
    return t;
  }

  private getRandomInt(min: number = 0, max: number = 10): number {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
  }

  private generateParamValues(params: Param[]): ParamValue[] {
    let values: ParamValue[] = [];

    for (let param of params) {
      let valuesCount = this.getRandomInt(1000);
      for (let i = 0; i < valuesCount; i++) {
        values.push(
          new ParamValue(
            uuid(),
            param.uid,
            this.instanceUid,
            this.getRandomInt(1000).toString(),
            this.toDateTime(this.getRandomInt(100000))
          )
        );
      }
    }

    return values;
  }

  ngOnInit() {}
}
