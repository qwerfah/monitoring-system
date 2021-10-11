import { HttpMethod } from './http-method';

export class OperationRecord {
  constructor(
    public uid: string,
    public userName: string | null,
    public serviceId: string,
    public route: string,
    public method: HttpMethod,
    public status: number,
    public elapsed: number,
    public time: Date
  ) {}
}
