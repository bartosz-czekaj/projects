using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using API.Services;
using Common.Consul;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers
{
    [Route("[controller]")]
    [ApiController]
    public class ValuesController : ControllerBase
    {
        private readonly  IConsulHttpClient _consulHttpClient;
        private readonly IMikroService _mikroService;
        // GET api/valuesIConsulHttpClient

        public ValuesController(IConsulHttpClient consulHttpClient, IMikroService mikroService)
        {
            _consulHttpClient = consulHttpClient;
            _mikroService = mikroService;
        }

        [HttpGet]
        public ActionResult<IEnumerable<string>> Get()
        {
            return new string[] { "value1", "value2" };
        }

        [HttpGet("ms")]
        public async Task<ActionResult<IList<string>>> FromMs()
        {
            var response = await _consulHttpClient.GetAsync("mikro/api/values");
            return Ok(response);
        }

        [HttpGet("msre")]
        public async Task<ActionResult<IList<string>>> FromMsRs()
        {
            //return new List<string>(){ "value1", "value2","value1", "value2","value1", "value2" };
             var response = await _mikroService.GetAsync();
            Console.WriteLine("------------------");
            Console.WriteLine(response);
            Console.WriteLine("------------------");
            return Ok(response);
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
