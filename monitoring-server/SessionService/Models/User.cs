using System;

namespace SessionService.Models
{
    /// <summary>
    /// Represents monitoring system user.
    /// </summary>
    public class User
    {
        /// <summary>
        /// Internal user identifier.
        /// </summary>
        public long Id { get; set; }
        /// <summary>
        /// External user identifier.
        /// </summary>
        public Guid UserUid { get; set; }
        /// <summary>
        /// User login.
        /// </summary>
        public string Login { get; set; }
        /// <summary>
        /// User password hash.
        /// </summary>
        public string PasswordHash { get; set; }
        /// <summary>
        /// Password salt to hash.
        /// </summary>
        public byte[] Salt { get; set; }
    }
}
