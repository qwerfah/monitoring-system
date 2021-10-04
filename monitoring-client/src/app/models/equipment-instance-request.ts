import { InstanceStatus } from './instance-status';

export class EquipmentInstanceRequest {
  constructor(public name: string, public description: string | null, public status: InstanceStatus) {}
}
