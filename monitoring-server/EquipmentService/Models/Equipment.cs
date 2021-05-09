using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace EquipmentService.Models
{
    /// <summary>
    /// Represents particular equipment instance.
    /// </summary>
    public class Equipment
    {
        /// <summary>
        /// Internal equipment instance identifier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External equipment instance identifier.
        /// </summary>
        public long EquipmentUid { get; set; }
        /// <summary>
        /// Equipment model identifier that this instance belongs to.
        /// </summary>
        public long EquipmentModelId { get; set; }
        /// <summary>
        /// Equipment instance operational status.
        /// </summary>
        public EquipmentStatus Status { get; set; }
        /// <summary>
        /// Navigation property to equipment model instance.
        /// </summary>
        public virtual EquipmentModel EquipmentModel { get; set; }
    }
}
