using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Principal;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http.Filters;

namespace Interview.Auth
{
    [AttributeUsage(AttributeTargets.Method, AllowMultiple = false)]
    public class JwtAuthenticationAttribute : Attribute, IAuthenticationFilter
    {
        public string Realm { get; set; }
        public bool AllowMultiple => false;

        private readonly string _userRole;

        public JwtAuthenticationAttribute(string userRole = "user")
        {
            _userRole = userRole;
        }

        public async Task AuthenticateAsync(HttpAuthenticationContext context, CancellationToken cancellationToken)
        {
            HttpCookie jwtCookie = HttpContext.Current.Request.Cookies[".JWTAUTH"];
            string userData = string.Empty;
            var request = context.Request;

            if (jwtCookie == null)
            {
                context.ErrorResult = new AuthenticationFailureResult("Token not set", request);
                return;
            }

            userData = jwtCookie.Value;

            if (String.IsNullOrEmpty(userData))
            {
                context.ErrorResult = new AuthenticationFailureResult("Empty token", request);
                return;
            }

            var principal = await AuthenticateJwtToken(userData);


            if (principal == null)
            {
                context.ErrorResult = new AuthenticationFailureResult("Invalid token", request);
            }
            else
            {
                context.Principal = principal;
            }

            principal.IsInRole(_userRole);
        }

        private TokenValidationParameters GetTokenValidationParameters()
        {
            return new TokenValidationParameters()
            {
                ValidateIssuer = false,
                ValidateAudience = false,
                IssuerSigningKey = GetSymmetricSecurityKey()
            };
        }

        private SecurityKey GetSymmetricSecurityKey()
        {
            byte[] symmetricKey = Convert.FromBase64String(Secret.SecretKey);
            return new SymmetricSecurityKey(symmetricKey);
        }


        private bool ValidateToken(string token, out string username, out string userrole)
        {
            username = string.Empty;
            userrole = string.Empty;

            var simplePrinciple = JwtManager.GetPrincipal(token);
            var identity = simplePrinciple?.Identity as ClaimsIdentity;

            if (identity == null)
                return false;

            if (!identity.IsAuthenticated)
                return false;

            var usernameClaim = identity.FindFirst(ClaimTypes.Name);
            username = usernameClaim?.Value;

            if (string.IsNullOrEmpty(username))
                return false;

            var actorClaim = identity.FindFirst(ClaimTypes.Actor);
            userrole = actorClaim?.Value;

            if (string.IsNullOrEmpty(userrole))
                return false;

            if(userrole != _userRole)
            {
                return false;
            }

            // More validate to check whether username exists in system

            return true;
        }

        protected Task<IPrincipal> AuthenticateJwtToken(string token)
        {
            TokenValidationParameters tokenValidationParameters = GetTokenValidationParameters();

            JwtSecurityTokenHandler jwtSecurityTokenHandler = new JwtSecurityTokenHandler();
            try
            {
                string username;
                string userrole;
                if (ValidateToken(token, out username, out userrole))
                {
                    var claims = new List<Claim>
                    {
                        new Claim(ClaimTypes.Name, username),
                        new Claim(ClaimTypes.Actor, userrole)
                    };

                    var identity = new ClaimsIdentity(claims, "Jwt",string.Empty, userrole);
                    IPrincipal user = new ClaimsPrincipal(identity);

                    return Task.FromResult(user);
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }

            return Task.FromResult<IPrincipal>(null);
        }

        public Task ChallengeAsync(HttpAuthenticationChallengeContext context, CancellationToken cancellationToken)
        {
            //var challenge = new AuthenticationHeaderValue("Basic");
            //context.Result = new AddChallengeOnUnauthorizedResult(challenge, context.Result);
            return Task.FromResult(0);
        }
    }
}