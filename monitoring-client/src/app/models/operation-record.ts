import { HttpMethod } from './http-method';

export class OperationRecord {
  constructor(
    public uid: string,
    userName: string | null,
    serviceId: string,
    route: string,
    method: HttpMethod,
    status: number,
    elapsed: number,
    time: Date
  ) {}
}
