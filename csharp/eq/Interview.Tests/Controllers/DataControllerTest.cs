using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Interview.Controllers;
using Interview.Repository;
using Interview.Serializer;
using Interview.Models;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Web.Http.Results;
using FluentAssertions;

namespace Interview.Tests.Controllers
{
    [TestClass]
    public class DataControllerTest
    {
        private IDataRepository _dataRepo;
        private ISerializer _serializer;
        static HttpClient _client = new HttpClient();

        [TestInitialize]
        public void Init()
        {
            _serializer = new SerializerImp(@"C:\Projects\C#\testEQ\Interview.Tests\Data\data.json");
            _dataRepo = new DataRepository(_serializer);
        }

        [TestMethod]
        public async Task WhenYouAddDataTheDataShouldBeadded()
        {
            var expected = new Data()
            {
                Id = Guid.NewGuid(),
                ApplicationId = 55221,
                Type = "test",
                Summary = "summary",
                Amount = 22.33M,
                PostingDate = DateTime.Now,
                IsCleared = false,
                ClearedDate = DateTime.Now
            };

            DataController dc = new DataController(_dataRepo);
            var ret = await dc.Post(expected);

            var contentRes = ret as OkNegotiatedContentResult<string>;
            Assert.IsNotNull(contentRes);

            var given = JsonConvert.DeserializeObject<Data>(contentRes.Content);


            expected.Should().Equals(given);
        }

        [TestMethod]
        public async Task WhenYouAddDataTwiceTheDataShouldBeAddedOnlyOnce()
        {
            var expected = new Data()
            {
                Id = Guid.NewGuid(),
                ApplicationId = 55221,
                Type = "test",
                Summary = "summary",
                Amount = 22.33M,
                PostingDate = DateTime.Now,
                IsCleared = false,
                ClearedDate = DateTime.Now
            };

            DataController dc = new DataController(_dataRepo);
            var ret = await dc.Post(expected);

            var contentRes = ret as OkNegotiatedContentResult<string>;
            Assert.IsNotNull(contentRes);

            var given = JsonConvert.DeserializeObject<Data>(contentRes.Content);

            expected.Should().Equals(given);

            ret = await dc.Post(expected);
            var notFoud = ret as NotFoundResult;

            Assert.IsNotNull(contentRes);
        }
     }
}
