class EquipmentInstance {
  constructor(
    public uid: string,
    public modelUid: string,
    public name: string,
    public description: string | null,
    public status: InstanceStatus
  ) {}
}
