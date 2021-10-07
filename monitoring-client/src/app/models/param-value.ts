export class ParamValue {
  constructor(
    public uid: string,
    public paramUid: string,
    public instanceUid: string,
    public value: string,
    public time: Date
  ) {}
}
