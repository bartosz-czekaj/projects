
using System;
using System.Threading.Tasks;
using Actio.Common.Services;
using Actio.Common.Commands;
using Actio.Common.Events;
using Actio.Services.Activities.Services;
using Actio.Common.Exceptions;
using Microsoft.Extensions.Logging;
using RawRabbit;


namespace Actio.Services.Activities.Handlers
{
    public class CreateActivityHandler : ICommandHandler<CreateActivity>
    {
        private readonly IBusClient _busClient;
        private readonly IActivityService  _activityService;
        private readonly ILogger _logger;

        public CreateActivityHandler(IBusClient busClient, IActivityService activityService, ILogger<CreateActivityHandler> logger)
        {
            _busClient = busClient;
            _activityService = activityService;
           _logger = logger;
        }
        public async Task HandleAsync(CreateActivity command)
        {

            var res = "failed";
            try
            {
                await _activityService.AddAsync(command.Id, command.UserId, command.Category, command.Name, command.Description, command.CreatedAt);
                await _busClient.PublishAsync(new ActivityCreated(command.UserId, command.Id, command.Category, command.Name, command.Description));

                res = "success";
            }
            catch(ActioException ex)
            {
                await _busClient.PublishAsync(new CreateActivityRejected(command.Id, ex.Code, ex.Message));
                _logger.LogError(ex.Message);
            }
            catch(Exception ex)
            {
                 await _busClient.PublishAsync(new CreateActivityRejected(command.Id, ex.GetType().ToString(), ex.Message));
                 _logger.LogError(ex.Message);
            }

            _logger.LogInformation($"Creating activity: {command.Category} {command.Name} : {res}");
            Console.WriteLine($"Creating activity: {command.Category} {command.Name} : {res}");
            
        }
    }
}