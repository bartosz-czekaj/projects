using System;
using System.Linq;
using System.Threading.Tasks;
using Actio.Api.Repositories;
//using Actio.Api.Repositories;
using Actio.Common.Commands;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using RawRabbit;

namespace Actio.Api.Controllers
{
    [Route("[controller]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    public class ActivitiesController : Controller
    {
        private readonly IBusClient _busClient;
        private readonly IActivityRepository _activityRepository;

        private Guid CurrentUserId() => Guid.Parse(User.Identity.Name);
        public ActivitiesController(IBusClient busClient, IActivityRepository activityRepository)
        {
            _busClient = busClient;
            _activityRepository = activityRepository;
        }

        [HttpPost("")]
        public async Task<IActionResult> Post([FromBody]CreateActivity command)
        {
            if(command == null)
            {
                return BadRequest();
            }

            command.Id = Guid.NewGuid();
            command.CreatedAt = DateTime.UtcNow;
            command.UserId = CurrentUserId();

            await _busClient.PublishAsync(command);

            return Accepted($"activities/{command.Id}");
        }

        [HttpGet("")]
        public async Task<IActionResult> Get()
        {
            Console.WriteLine("CUID: " + CurrentUserId());
            Console.WriteLine("User.Identity: " + User.Identity);
            Console.WriteLine("User.Identity.Name: " + User.Identity.Name);
            var activities = await _activityRepository.BrowseAsync(CurrentUserId());

            return Json(activities.Select(x=> new {x.Id, x.Name, x.Category, x.CreatedAt}));
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> Get(Guid id)
        {
            var activity = await _activityRepository.GetAsync(id);
            if(activity == null)
            {
                return NotFound();
            }

            if(activity.UserId != CurrentUserId())
            {
                return Unauthorized();
            }
            
            var activities = await _activityRepository.GetAsync(CurrentUserId());

            return Json(activities);
        }
    }
}