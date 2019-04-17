﻿using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using Polly;
//using Polly.Contrib.TimingPolicy;

namespace polly
{
    class Program
    {
        static async Task Main(string[] args)
        {
            const string TestClient = "TestClient";

            IServiceCollection services = new ServiceCollection();

            services.AddHttpClient(TestClient)
                .AddPolicyHandler(AsyncTimingPolicy<HttpResponseMessage>.Create(PublishTiming));

            HttpClient configuredClient =
                services
                    .BuildServiceProvider()
                    .GetRequiredService<IHttpClientFactory>()
                    .CreateClient(TestClient);

           
            var urls = new List<string>
            {
                "https://www.google.com/",
                "https://www.google.co.uk/",
                "https://www.bbc.co.uk/"
            };

            urls.ForEach(async url=>{
                var context = new Context {["url"] = url};
                HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Get, url);
                request.SetPolicyExecutionContext(context);

                var result = await configuredClient.SendAsync(request, default(CancellationToken));    
            });

            Console.ReadKey();

        }

        static Task PublishTiming(TimeSpan executionDuration, Context context)
        {
            object url = "[unknown]";
            if (context?.TryGetValue("url", out url) ?? false)
            {
                Console.WriteLine($"Took {executionDuration.TotalSeconds:0.###} seconds retrieving {url}");
            }
            return Task.CompletedTask;
        }
    }
}
