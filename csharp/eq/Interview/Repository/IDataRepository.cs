using Interview.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Interview.Repository
{
    public interface IDataRepository
    {
        Task<Data> GetAsync(Guid id);
        Task<IDictionary<Guid,Data>> BrowseAsync();
        Task<Data> AddAsync(Data model);
        Task<Data> RemoveAsync(Guid model);
    }
}
