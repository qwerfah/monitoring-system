import { InstanceStatus } from './instance-status';

export class EquipmentInstance {
  constructor(
    public uid: string,
    public modelUid: string,
    public name: string,
    public modelName: string,
    public description: string | null,
    public status: InstanceStatus
  ) {}
}
