using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace Actio.Services.Activities.Controllers
{
    [Route("")]
    public class HomeController : Controller
    {

       [HttpGet("")]
       public IActionResult GetAction() => Content("Hello from Actio.Services.Activites API!");
    }
}