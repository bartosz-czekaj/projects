using System;

namespace common.vault
{
    public class VaultAuthTypeNotSupportedException : Exception
    {
        public string AuthType { get; set; }
        
        public VaultAuthTypeNotSupportedException(string authType) : this(string.Empty, authType)
        {
        }

        public VaultAuthTypeNotSupportedException(string message, string authType) : base(message)
        {
            AuthType = authType;
        }
    }
}