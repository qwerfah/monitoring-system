export class ModelStat {
  constructor(
    public modelUid: string,
    public modelName: string,
    public paramCount: number,
    public instanceCount: number,
    public activeInstanceCount: number,
    public inactiveInstanceCount: number,
    public decommissionedInstanceCount: number,
    public monitorCount: number
  ) {}
}
