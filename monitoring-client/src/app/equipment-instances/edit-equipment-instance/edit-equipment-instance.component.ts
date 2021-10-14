import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EquipmentInstanceRequest } from 'src/app/models/equipment-instance-request';
import { InstanceStatus } from 'src/app/models/instance-status';

@Component({
  selector: 'app-edit-equipment-instance',
  templateUrl: './edit-equipment-instance.component.html',
  styleUrls: ['./edit-equipment-instance.component.css'],
})
export class EditEquipmentInstanceComponent implements OnInit {
  @Output() addEvent = new EventEmitter<EquipmentInstanceRequest | null>();

  InstanceStatus = InstanceStatus;

  instanceForm: FormGroup;

  @Input() name: string;
  @Input() description: string | null;
  @Input() status: InstanceStatus;

  constructor(private fb: FormBuilder) {
    this.instanceForm = fb.group({
      name: [this.name, [Validators.required, Validators.minLength(3)]],
      description: [this.description, [Validators.minLength(0)]],
    });
  }

  ngOnInit() {}

  setStatus(status: InstanceStatus): void {
    this.status = status;
  }

  submit() {
    if (this.instanceForm.invalid) {
      this.instanceForm.markAllAsTouched();
      return;
    }

    let instance = new EquipmentInstanceRequest(
      this.instanceForm.controls.name.value,
      this.instanceForm.controls.description.value,
      this.status
    );

    this.addEvent.emit(instance);
  }

  cancel() {
    this.addEvent.emit(null);
  }
}
