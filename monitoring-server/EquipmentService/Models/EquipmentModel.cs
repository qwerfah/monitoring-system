using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace EquipmentService.Models
{
    /// <summary>
    /// Represents equipment model.
    /// </summary>
    public class EquipmentModel
    {
        /// <summary>
        /// Internal equipment model identifier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External equipment model identifier.
        /// </summary>
        public Guid EquipmentModelUid { get; set; }
        /// <summary>
        /// Equipment model name.
        /// </summary>
        public string Name { get; set; }
        /// <summary>
        /// Navigation property to list of model params.
        /// </summary>
        public virtual List<Param> Params { get; set; }
        /// <summary>
        /// Navigation property to list of model instances.
        /// </summary>
        public virtual List<Equipment> Equipments { get; set; }
    }
}
