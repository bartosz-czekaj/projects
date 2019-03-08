using System;

namespace Actio.Common.Auth
{
    public interface IJWTHandler
    {
         JsonWebToken Create(Guid userId);
    }
}