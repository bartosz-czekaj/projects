using Microsoft.AspNetCore.Mvc;

namespace API.Controllers
{
    [Route("")]
    public class HomeController : ControllerBase
    {
        [HttpGet]
        public IActionResult Get() => Ok("DShop Discounts Service");

        [HttpGet("ping")]
        public IActionResult Ping() => Ok("I'm alive");
    }
}