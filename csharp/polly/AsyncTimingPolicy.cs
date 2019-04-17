using System;
using System.Diagnostics;
using System.Threading;
using System.Threading.Tasks;
using Polly;


namespace polly
{
    public class AsyncTimingPolicy<TResult> : AsyncPolicy<TResult>
    {
        private static readonly double TimestampToTicks = TimeSpan.TicksPerSecond / (double)Stopwatch.Frequency;
        private readonly Func<TimeSpan, Context, Task> timingPublisher;

        public static AsyncTimingPolicy<TResult> Create(Func<TimeSpan, Context, Task> timingPublisher) 
=> new AsyncTimingPolicy<TResult>(timingPublisher);

        public AsyncTimingPolicy(System.Func<TimeSpan, Context, Task> timingPublisher)
        {
            this.timingPublisher = timingPublisher ?? throw new ArgumentNullException(nameof(timingPublisher));
        }

        protected override async Task<TResult> ImplementationAsync(
            Func<Context, CancellationToken, Task<TResult>> action, 
            Context context, 
            CancellationToken cancellationToken,
            bool continueOnCapturedContext)
        {
            long before = Stopwatch.GetTimestamp();
            try {
                return await action(context, cancellationToken).ConfigureAwait(continueOnCapturedContext);
            }
            finally
            {
                long elapsed = Stopwatch.GetTimestamp() - before;
                TimeSpan elapsedAsTimeSpan = new TimeSpan((long)(TimestampToTicks * elapsed));
                await timingPublisher(elapsedAsTimeSpan, context)
                    .ConfigureAwait(continueOnCapturedContext); // [*]
            }
        }
    }
}