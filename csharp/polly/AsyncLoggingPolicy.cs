using System;
using System.Diagnostics;
using System.Threading;
using System.Threading.Tasks;
using Polly;
using Polly.Utilities;

public class AsyncLoggingPolicy<TResult> //: AsyncPolicy<TResult>
{
    /* protected override async Task<TResult> ImplementationAsync(Func<Context, CancellationToken, Task<TResult>> action, 
                                                         Context context,
                                                         CancellationToken cancellationToken, 
                                                         bool continueOnCapturedContext)
    {
        try
        {
            TResult result = await action(context, cancellationToken).ConfigureAwait(continueOnCapturedContext);

            if (!ResultPredicates.AnyMatch(result))
            {
                return result;
            }

            Log(context, result);

            return result;
        }
        catch (Exception ex)
        {
            var handleException = ExceptionPredicates.FirstMatchOrDefault(ex);
            if(handleException is null)
            {
                throw;
            }

            Log(context, ex);

            handleException.RethrowWithOriginalStackTraceIfDiffersFrom(ex);

            throw;
        }

    }

    private static void Log<T>(Context context, T data)
    {
        ILogger logger = LoggerProvider(context);
        LogAction(logger, context, new DelegateResult<T>(data));
    }*/
}