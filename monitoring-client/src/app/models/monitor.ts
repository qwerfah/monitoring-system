export class Monitor {
  constructor(
    public uid: string,
    public instanceUid: string,
    public modelUid: string,
    public name: string,
    public description: string | null
  ) {}
}