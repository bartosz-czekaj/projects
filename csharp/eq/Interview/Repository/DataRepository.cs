using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Interview.Models;
using Interview.Serializer;

namespace Interview.Repository
{
    public class DataRepository : IDataRepository
    {
        private static ISerializer _serializer;
        private Lazy<IDictionary<Guid, Data>> lazyData = new Lazy<IDictionary<Guid, Data>>(()=>_serializer.Deserialize());


        public DataRepository(ISerializer serializer)
        {
            _serializer = serializer;
        }

        public Task<Data> AddAsync(Data data)
        {
            Data tmpdata;
            if(!lazyData.Value.TryGetValue(data.Id, out tmpdata))
            {
                lazyData.Value.Add(data.Id, data);

                return Task.FromResult(data);
            }

            return Task.FromResult(null as Data);
        }

        public Task<IDictionary<Guid, Data>> BrowseAsync()
        {
            return Task.FromResult(lazyData.Value);
        }

        public Task<Data> GetAsync(Guid id)
        {
            Data data;
            lazyData.Value.TryGetValue(id, out data);

            return Task.FromResult(data);
        }

        public Task<Data> RemoveAsync(Guid id)
        {
            Data data;
            if(lazyData.Value.TryGetValue(id, out data))
            {
                lazyData.Value.Remove(id);
            }

            return Task.FromResult(data);

        }
    }
}
