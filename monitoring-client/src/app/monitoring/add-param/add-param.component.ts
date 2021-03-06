import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { Monitor } from 'src/app/models/monitor';
import { Param } from 'src/app/models/param';
import { EquipmentService } from 'src/app/services/equipment.service';

@Component({
  selector: 'app-add-param',
  templateUrl: './add-param.component.html',
  styleUrls: ['./add-param.component.css'],
})
export class AddParamComponent implements OnInit {
  @Output() addEvent = new EventEmitter<string[] | null>();

  isLoading: boolean = true;

  paramsControl = new FormControl();

  @Input() monitor: Monitor;
  @Input() trackedParams: Param[];

  params: Param[];

  constructor(private equipmentService: EquipmentService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.equipmentService.getInstanceParams(this.monitor.instanceUid, this.snackBar).subscribe((params) => {
      this.params = params.filter((p1) => this.trackedParams.findIndex((p2) => p2.uid === p1.uid) < 0);
      this.isLoading = false;
    });
  }

  submit() {
    let params = (this.paramsControl.value as Param[]).map((p: Param) => p.uid);
    this.addEvent.emit(params);
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
