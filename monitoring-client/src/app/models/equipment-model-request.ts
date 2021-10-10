import { ParamRequest } from './param-request';

export class EquipmentModelRequest {
  constructor(public name: string, public description: string | null, public params: ParamRequest[] | null) {}
}
