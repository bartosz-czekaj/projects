using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using common;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;

namespace vault_micro.Controllers
{
    [Route("")]
    [ApiController]
    public class ValuesController : ControllerBase
    {
        private readonly IOptions<AppOptions> _appOptions;
        public ValuesController(IOptions<AppOptions> appOptions)
        {
            _appOptions = appOptions;
        } 

        // GET api/values
        [HttpGet]
        public IActionResult Get()
        {
            Console.WriteLine("=========================");
            Console.WriteLine(_appOptions.Value.Name);
            Console.WriteLine("=========================");
            return Ok(_appOptions.Value.Name);;
        }

        // GET api/values/5
        [HttpGet("{id}")]
        public ActionResult<string> Get(int id)
        {
            return "value";
        }

        // POST api/values
        [HttpPost]
        public void Post([FromBody] string value)
        {
        }

        // PUT api/values/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody] string value)
        {
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
