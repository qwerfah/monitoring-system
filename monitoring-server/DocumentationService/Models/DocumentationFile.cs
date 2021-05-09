using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace DocumentationService.Models
{
    /// <summary>
    /// Represents documentation file for particular equipment model.
    /// </summary>
    public class DocumentationFile
    {
        /// <summary>
        /// Internal file identifier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External file identifier.
        /// </summary>
        public Guid FileUid { get; set; }
        /// <summary>
        /// Equipment model external identifier that file belongs to.
        /// </summary>
        public Guid EquipmentModelUid { get; set; }
        /// <summary>
        /// File name.
        /// </summary>
        public string Name { get; set; }
        /// <summary>
        /// File content.
        /// </summary>
        public byte[] Content { get; set; }
    }
}
