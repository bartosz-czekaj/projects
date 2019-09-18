using Interview.Auth;
using Interview.Models;
using Interview.Repository;
using Newtonsoft.Json;
using System;
using System.Threading.Tasks;
using System.Web.Http;

namespace Interview.Controllers
{
    public class DataController : ApiController
    {
        private readonly IDataRepository _dataRepository;

        public DataController(IDataRepository dataRepository)
        {
            _dataRepository = dataRepository;
        }

        // GET api/<controller>
        [JwtAuthentication("admin")]
        public async Task<IHttpActionResult> Get()
        {
            var allData = await _dataRepository.BrowseAsync();

            if (allData != null)
            {
                return Ok(JsonConvert.SerializeObject(allData));
            }
            return NotFound();
        }

        // GET api/<controller>/5
        [JwtAuthentication("admin")]
        public async Task<IHttpActionResult> Get(Guid id)
        {
            return await DoAction(_dataRepository.GetAsync, id);
        }

        // POST api/<controller>
        [JwtAuthentication("admin")]
        public async Task<IHttpActionResult> Post([FromBody]Data value)
        {
            return await DoAction(_dataRepository.AddAsync, value);
        }

        // PUT api/<controller>/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/<controller>/5
        [JwtAuthentication("admin")]
        public async Task<IHttpActionResult> Delete(Guid id)
        {
            return await DoAction(_dataRepository.RemoveAsync, id);
        }

        private async Task<IHttpActionResult> DoAction<T>(Func<T, Task<Data>> funct, T item)
        {
            var data = await funct(item);

            if (data != null)
            {
                return Ok(JsonConvert.SerializeObject(data));
            }
            return NotFound();
        }

    }
}