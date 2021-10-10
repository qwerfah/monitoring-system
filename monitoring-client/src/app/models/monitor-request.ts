export class MonitorRequest {
  constructor(public name: string, public description: string | null, public params: string[]) {}
}
