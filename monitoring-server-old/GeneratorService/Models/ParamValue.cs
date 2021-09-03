using System;

namespace GeneratorService.Models
{
    /// <summary>
    /// Represents value of particular equipment instance param.
    /// </summary>
    public class ParamValue
    {
        /// <summary>
        /// Internal param value identifier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External param value identifier.
        /// </summary>
        public Guid ParamValueUid { get; set; }
        /// <summary>
        /// Equipment instance external identifier that value belongs to.
        /// </summary>
        public Guid EquipmentUid { get; set; }
        /// <summary>
        /// Equipment param external identifier that value belongs to.
        /// </summary>
        public Guid ParamUid { get; set; }
        /// <summary>
        /// Param value.
        /// </summary>
        public string Value { get; set; }
    }
}
