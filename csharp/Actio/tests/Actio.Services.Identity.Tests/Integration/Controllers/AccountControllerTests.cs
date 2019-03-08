using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Actio.Common.Auth;
using Actio.Common.Exceptions;
using Actio.Services.Identity;
using FluentAssertions;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.TestHost;
using Newtonsoft.Json;
using Xunit;

namespace Actio.Services.Activities.Tests.Integration.Controllers
{
    public class AccountControllerTests
    {
        private readonly TestServer _server;
        private readonly HttpClient _client;
        
        public AccountControllerTests()
        {
            _server = new TestServer(WebHost.CreateDefaultBuilder()
                .UseStartup<Startup>());
            _client = _server.CreateClient();
        }

        [Fact]
        public async Task account_controller_bad_login_should_throws_an_exception()
        {
            var payload = GetPayload(new {email = "user1@email.com", password = "secret"});
            var exception = Record.ExceptionAsync(async () => await _client.PostAsync("login", payload));

            exception.Should().NotBeNull();
            exception.Result.Should().NotBeNull();
            exception.Result.Should().BeOfType<ActioException>();
            exception.Result.Message.Should().Be("Invalid credentials.");
        }

        [Fact]
        public async Task account_controller_login_should_return_json_web_token()
        {
            var payload = GetPayload(new {email = "m@wp.pl", password = "pass"});
            
            var response = await _client.PostAsync("login", payload);
            response.EnsureSuccessStatusCode();

            var content = await response.Content.ReadAsStringAsync();
            var jwt = JsonConvert.DeserializeObject<JsonWebToken>(content);

            jwt.Should().NotBeNull();
            jwt.Token.Should().NotBeEmpty();
            jwt.Expires.Should().BeGreaterThan(0);
        }

        private static StringContent GetPayload(object data)
        {
            var json = JsonConvert.SerializeObject(data);

            return new StringContent(json, Encoding.UTF8, "application/json");
        }
    }
}