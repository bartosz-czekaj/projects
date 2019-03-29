using Microsoft.AspNetCore.Mvc;

namespace microservice.Controllers
{
    [Route("")]
    public class HomeController : ControllerBase
    {
        [HttpGet]
        public IActionResult Get() => Ok("MIKROSERWIS");

        [HttpGet("ping")]
        public IActionResult Ping() => Ok("i'm alive");
    }
}