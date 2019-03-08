using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.DependencyInjection;
using Actio.Common.Events;
using Actio.Common.Services;
using RawRabbit;

namespace Actio.Api
{
    public class Program
    {
        public static void Main(string[] args)
        {   
            Console.WriteLine(args);
            ServiceHost.Create<Startup>(args)
            .UseRabbitMq()
            .SubscribeToEvent<ActivityCreated>()
            .Build()
            .Run();
        }
    }
}
