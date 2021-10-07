export class Monitor {
  constructor(
    public uid: string,
    public instanceUid: string,
    public modelUid: string,
    public name: string,
    public instanceName: string,
    public modelName: string,
    public description: string | null
  ) {}
}
