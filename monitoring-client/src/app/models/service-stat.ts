export class ServiceStat {
  constructor(
    public serviceId: string,
    public requestCount: number,
    public successCount: number,
    public userErrorCount: number,
    public serverErrorCount: number,
    public avgRequestTime: number
  ) {}
}
