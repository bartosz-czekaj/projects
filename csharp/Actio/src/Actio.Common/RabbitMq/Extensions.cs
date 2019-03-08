using System;
using System.Reflection;
using System.Threading.Tasks;
using Actio.Common.Commands;
using Actio.Common.Events;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using RawRabbit;
using RawRabbit.Instantiation;
using RawRabbit.Pipe;

namespace Actio.Common.RabbitMq
{
    public static class Extensions
    {
        public static Task WithEventHandlerAsync<TEvent>(this IBusClient busClient, IEventHandler<TEvent> eventHandler) where TEvent : IEvent
        {
            Console.WriteLine("ALA 2 MA KOTA");
            return busClient.SubscribeAsync<TEvent>(
                    msg=>eventHandler.HandleAsync(msg), 
                    ctx=>ctx.UseSubscribeConfiguration(
                        cfg=>cfg.FromDeclaredQueue(
                        queue=>queue.WithName(GetQueueName<TEvent>())
                        )
                    ));
        }

        public static Task WithCommandHandlerAsync<TCommand>(this IBusClient busClient, ICommandHandler<TCommand> commandHandler) where TCommand : ICommand
        {
            Console.WriteLine("ALA MA KOTA");

            return busClient.SubscribeAsync<TCommand>(
                    msg=>commandHandler.HandleAsync(msg), 
                    ctx=>ctx.UseSubscribeConfiguration(
                        cfg=>cfg.FromDeclaredQueue(
                        queue=>queue.WithName(GetQueueName<TCommand>())
                        )
                    ));
        }

        public static void AddRabbitMq(this IServiceCollection serviceCollection, IConfiguration configuration)
        {
            var options = new RabbitmqOptions();

            var section = configuration.GetSection("rabbitmq");
            section.Bind(options);

            Console.WriteLine(section);

            var client = RawRabbitFactory.CreateSingleton(new RawRabbitOptions{
                ClientConfiguration = options
            });

            serviceCollection.AddSingleton<IBusClient>(_ => client);
        }
        private static string GetQueueName<T>() => $"{Assembly.GetEntryAssembly().GetName()}/{typeof(T).Name}";

    }
}