import { Component, OnInit } from '@angular/core';
import { v4 as uuid } from 'uuid';

import { OperationRecord } from 'src/app/models/operation-record';
import { ReportingService } from 'src/app/services/reporting.service';
import { HttpMethod } from 'src/app/models/http-method';
import { ServiceStat } from 'src/app/models/service.stat';
import { ModelStat } from 'src/app/models/model.stat';

@Component({
  selector: 'app-reporting-info',
  templateUrl: './reporting-info.component.html',
  styleUrls: ['./reporting-info.component.css'],
})
export class ReportingInfoComponent implements OnInit {
  operationRecords: OperationRecord[];
  serviceStats: ServiceStat[];
  modelStats: ModelStat[];

  isLoading: boolean = false;

  constructor(private reportingService: ReportingService) {
    this.operationRecords = [];
    this.serviceStats = [];
    this.modelStats = [];
  }

  ngOnInit() {
    this.generateRecords();
    this.generateModelStats();
    this.serviceStats = this.getServiceIds().map((serviceId) => this.getServiceStats(serviceId));
  }

  private getRandomInt(min: number = 0, max: number = 10): number {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
  }

  private onlyUnique(value: string, index: number, self: string[]): boolean {
    return self.indexOf(value) === index;
  }

  getServiceIds(): string[] {
    return this.operationRecords.map((rec) => rec.serviceId).filter(this.onlyUnique);
  }

  getServiceStats(serviceId: string): ServiceStat {
    let serviceOperations = this.operationRecords.filter((op) => op.serviceId === serviceId);
    return new ServiceStat(
      serviceId,
      serviceOperations.length,
      serviceOperations.filter((op) => op.status < 400).length,
      serviceOperations.filter((op) => op.status >= 400 && op.status < 500).length,
      serviceOperations.filter((op) => op.status >= 500).length,
      Math.round(serviceOperations.map((op) => op.elapsed).reduce((a, b) => a + b, 0) / serviceOperations.length)
    );
  }

  generateRecords(): void {
    let services: string[] = ['session', 'equipment', 'documentation', 'monitoring', 'generator'];
    let codes: number[] = [200, 201, 400, 401, 403, 404, 409, 422, 500];
    let methods: HttpMethod[] = [HttpMethod.Get, HttpMethod.Post, HttpMethod.Patch, HttpMethod.Delete];
    this.operationRecords = [];

    for (let i = 0; i < 1000; i++) {
      this.operationRecords.push(
        new OperationRecord(
          uuid(),
          null,
          services[this.getRandomInt(0, 5)],
          'route',
          methods[this.getRandomInt(0, 4)],
          codes[this.getRandomInt(0, 9)],
          this.getRandomInt(0, 1000),
          new Date()
        )
      );
    }
  }

  generateModelStats(): void {
    this.modelStats = [
      new ModelStat(
        uuid(),
        'model_1',
        this.getRandomInt(1, 20),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 100)
      ),
      new ModelStat(
        uuid(),
        'model_2',
        this.getRandomInt(1, 20),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 100)
      ),
      new ModelStat(
        uuid(),
        'model_3',
        this.getRandomInt(1, 20),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 100)
      ),
      new ModelStat(
        uuid(),
        'model_4',
        this.getRandomInt(1, 20),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 100)
      ),
      new ModelStat(
        uuid(),
        'model_5',
        this.getRandomInt(1, 20),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 100)
      ),
      new ModelStat(
        uuid(),
        'model_6',
        this.getRandomInt(0, 20),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 100)
      ),
      new ModelStat(
        uuid(),
        'model_7',
        this.getRandomInt(0, 20),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 50),
        this.getRandomInt(0, 100)
      ),
    ];
  }
}
