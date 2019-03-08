namespace Actio.Common.Auth
{
    public class JWTOptions
    {
        public string SecretKey {get; set;}
        public int ExpireMinutes {get; set;}
        public string Issuer {get; set;}
    }
}