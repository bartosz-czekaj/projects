using Interview.Auth;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Interview.Controllers
{
    public class UserController : ApiController
    {

        //public string Get(string userName)
        //{
        //    return "bbbbbbbbbbbbbbbbbbbb" + userName;
        //}

        //public string Get()
        //{
        //    return "aaaaaaaaaaa";
        //}

        // GET api/<controller>
        [AllowAnonymous]
        public string Get(string userName, string password)
        {
            string userRole;

            if (CheckUser(userName, password, out userRole))
            {
                return JwtManager.GenerateToken(userName, userRole);

            }

            throw new HttpResponseException(HttpStatusCode.Unauthorized);
        }

        public bool CheckUser(string userName, string password, out string userRole)
        {
            userRole = "admin";
            return true;
        }
    }
}