using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace StatisticsService.Models
{
    /// <summary>
    /// Represents service operation metadata.
    /// </summary>
    public class OperationRecord
    {
        /// <summary>
        /// Internal record identifier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External record identifier.
        /// </summary>
        public Guid OperationRecordUid { get; set; }
        /// <summary>
        /// Operation time.
        /// </summary>
        public DateTime OperationTime { get; set; }
        /// <summary>
        /// Service name that operation occured at.
        /// </summary>
        public string ServiceName { get; set; }
        /// <summary>
        /// Method name that operation represented by.
        /// </summary>
        public string MethodName { get; set; }
        /// <summary>
        /// Flag that shows if operation is successfull.
        /// </summary>
        public bool IsSuccessfull { get; set; }
        /// <summary>
        /// Status code returned by this operation.
        /// </summary>
        public ushort StatusCode { get; set; }
    }
}
