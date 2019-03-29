using System.Threading.Tasks;

namespace Common
{
    public interface IInitializer
    {
        Task InitializeAsync();
    }
}