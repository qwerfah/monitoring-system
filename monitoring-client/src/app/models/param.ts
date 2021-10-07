export class Param {
  constructor(
    public uid: string,
    public modelUid: string,
    public name: string,
    public measurmentUnits: string | null
  ) {}
}
