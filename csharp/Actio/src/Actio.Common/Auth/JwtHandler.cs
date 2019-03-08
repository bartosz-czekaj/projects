using System;
using System.IdentityModel.Tokens.Jwt;
using System.Text;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;

namespace Actio.Common.Auth
{
    public class JwtHandler : IJWTHandler
    {
        private readonly JwtSecurityTokenHandler  _jwtSecurityTokenHandler = new JwtSecurityTokenHandler();
        private readonly IOptions<JWTOptions> _options;
        private readonly SecurityKey _issuerSecurityKey;
        private readonly SigningCredentials _signingCredentials;
        private readonly JwtHeader _jwtHeader;
        private readonly TokenValidationParameters _tokenValidationParameters;

        public JwtHandler(IOptions<JWTOptions> options)
        {
            _options = options;
            _issuerSecurityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_options.Value.SecretKey));
            _signingCredentials = new SigningCredentials(_issuerSecurityKey, SecurityAlgorithms.HmacSha384);
            _jwtHeader = new JwtHeader(_signingCredentials);
            _tokenValidationParameters = new TokenValidationParameters
            {
                ValidateAudience = false,
                ValidIssuer = _options.Value.Issuer,
                IssuerSigningKey = _issuerSecurityKey
            };
        }

        public JsonWebToken Create(Guid userId)
        {
            var nowUtc = DateTime.UtcNow;
            var expires = nowUtc.AddMinutes(_options.Value.ExpireMinutes);

            Func<DateTime, long> getSec = (date) =>  (long)new TimeSpan(date.Ticks - new DateTime(1970,1,1).ToUniversalTime().Ticks).TotalSeconds;

            var exp = getSec(expires);
            var now = getSec(nowUtc);

            var payload = new JwtPayload
            {
                {"sub", userId},
                {"iss", _options.Value.Issuer},
                {"iat", now},
                {"exp", exp},
                {"unique_name", userId}
            };

            var jwt = new JwtSecurityToken(_jwtHeader, payload);
            var jwtoken = _jwtSecurityTokenHandler.WriteToken(jwt);

            return new JsonWebToken
            {
                Token = jwtoken,
                Expires = exp

            };

        }
    }
}
