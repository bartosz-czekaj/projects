using System.Collections.Generic;
using System.Threading.Tasks;
using RestEase;

namespace API.Services
{
    [SerializationMethods(Query = QuerySerializationMethod.Serialized)]
    public interface IMikroService
    {
        [AllowAnyStatusCode]
        [Get("api/values")]
        Task<IList<string>> GetAsync();  
        
    }
}