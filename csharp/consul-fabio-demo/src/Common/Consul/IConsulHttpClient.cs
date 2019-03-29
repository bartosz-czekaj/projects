using System.Collections.Generic;
using System.Threading.Tasks;

namespace Common.Consul
{
    public interface IConsulHttpClient
    {
        Task<T> GetAsync<T>(string requestUri);
        Task<IList<string>> GetAsync(string requestUri);
    }
}