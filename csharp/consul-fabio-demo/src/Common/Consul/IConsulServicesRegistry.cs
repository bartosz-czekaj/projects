using System.Threading.Tasks;
using Consul;

namespace Common.Consul
{
    public interface IConsulServicesRegistry
    {
         Task<AgentService> GetAsync(string name);
    }
}