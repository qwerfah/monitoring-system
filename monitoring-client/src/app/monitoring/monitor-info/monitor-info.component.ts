import { Component, OnInit } from '@angular/core';
import { Observable, of, Subject, zip } from 'rxjs';
import { v4 as uuid } from 'uuid';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { Monitor } from 'src/app/models/monitor';
import { Param } from 'src/app/models/param';
import { ParamValue } from 'src/app/models/param-value';
import { MonitoringService } from 'src/app/services/monitoring.service';
import { delay, repeat, repeatWhen } from 'rxjs/operators';

type ParamChartDataType = { name: string; series: { name: Date; value: number }[] };

@Component({
  selector: 'app-monitor-info',
  templateUrl: './monitor-info.component.html',
  styleUrls: ['./monitor-info.component.css'],
})
export class MonitorInfoComponent implements OnInit {
  monitorUid: string;
  instanceUid: string;

  isLoading: boolean = true;

  monitor: Monitor;
  params: Param[];
  paramValues: ParamValue[];

  chartData: ParamChartDataType[];

  constructor(
    private route: ActivatedRoute,
    private monitoringService: MonitoringService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.monitorUid = this.route.snapshot.params['uid'];

    zip(
      this.monitoringService.getMonitor(this.monitorUid),
      this.monitoringService.getMonitorParams(this.monitorUid)
    ).subscribe(
      (result) => {
        this.monitor = result[0];
        this.params = result[1];
        this.isLoading = false;
        this.downloadParamValues();
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок');
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис недоступен', 'Ок');
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: экран мониторинга не найден', 'Ок');
          }
        }
        this.isLoading = false;
      }
    );
  }

  downloadParamValues() {
    this.monitoringService
      .getMonitorParamValues(this.monitorUid)
      .pipe(repeatWhen((n) => n.pipe(delay(5000))))
      .subscribe(
        (values) => {
          this.paramValues = values;
          this.chartData = this.getParamUids().map((uid) => this.getParamValues(uid));
          this.isLoading = false;
        },
        (err: HttpErrorResponse) => {
          switch (err.status) {
            case 0: {
              this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок');
              break;
            }
            case 502: {
              this.snackBar.open('Ошибка: сервис недоступен', 'Ок');
              break;
            }
            case 404: {
              this.snackBar.open('Ошибка: экран мониторинга не найден', 'Ок');
            }
          }
          this.isLoading = false;
        }
      );
  }

  private onlyUnique(value: string, index: number, self: string[]): boolean {
    return self.indexOf(value) === index;
  }

  private getParamUids(): string[] {
    return this.paramValues.map((param) => param.paramUid).filter(this.onlyUnique);
  }

  private getParamValues(paramUid: string): ParamChartDataType {
    let series: { name: Date; value: number }[] = this.paramValues
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
}
