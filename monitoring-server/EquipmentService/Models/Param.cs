namespace EquipmentService.Models
{
    /// <summary>
    /// Represents equipment model param.
    /// </summary>
    public class Param
    {
        /// <summary>
        /// Internal equipment model param identifier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External equipment model param identifier.
        /// </summary>
        public long ParamUid { get; set; }
        /// <summary>
        /// Equipment model identifier that this param belongs to.
        /// </summary>
        public long EquipmentModelId { get; set; }
        /// <summary>
        /// Equipment model param name.
        /// </summary>
        public string Name { get; set; }
        /// <summary>
        /// Equipment model param measurement units.
        /// </summary>
        public string MeasurementUnits { get; set; }
        /// <summary>
        /// Navigation property to equipment model instance.
        /// </summary>
        public virtual EquipmentModel EquipmentModel { get; set; }
    }
}
