export class ServiceStat {
  constructor(
    public serviceId: string,
    public requestCount: number,
    public okCount: number,
    public userErrorCount: number,
    public serverErrorCount: number,
    public avgRequestTime: number
  ) {}
}
