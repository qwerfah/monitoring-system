using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MonitoringService.Models
{
    /// <summary>
    /// Represents monitor for particular equipment instance.
    /// Each instance of monitor can track single equipment 
    /// instance but specified set of instance params.
    /// </summary>
    public class Monitor
    {
        /// <summary>
        /// Internal monitor identidier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External monitor identifier.
        /// </summary>
        public Guid MonitorUid { get; set; }
        /// <summary>
        /// Monitor name.
        /// </summary>
        public string Name { get; set; }
        /// <summary>
        /// Equipment instance external identifier that monitor belongs to.
        /// </summary>
        public Guid EquipmentUid { get; set; }
        /// <summary>
        /// Equipment model external identifier that monitor belongs to.
        /// </summary>
        public Guid EquipmentModelUid { get; set; }
        /// <summary>
        /// Navigation property to all tracked params.
        /// </summary>
        public virtual List<MonitorParam> MonitorParams { get; set; }
    }
}
