using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MonitoringService.Models
{
    /// <summary>
    /// Represents auxiliary instance that describes
    /// connections between monitors and tracked params.
    /// </summary>
    public class MonitorParam
    {
        /// <summary>
        /// Internal monitor identifier.
        /// </summary>
        public long MonitorId { get; set; }
        /// <summary>
        /// External param identifier.
        /// </summary>
        public Guid ParamUid { get; set; }
        /// <summary>
        /// Navigation property to monitor instance.
        /// </summary>
        public virtual Monitor Monitor { get; set; }
    }
}
